/*
 * Copyright (c) [2024] [Murphy]
 * [Stock] is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan PubL v2.
 * You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 */

package tech.forethought.stock.service;

import lombok.extern.slf4j.Slf4j;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.data.annotation.Tran;
import tech.forethought.stock.adapter.client.HSLTClient;
import tech.forethought.stock.adapter.client.HSZBClient;
import tech.forethought.stock.adapter.client.HSZBLClient;
import tech.forethought.stock.adapter.client.HSZGClient;
import tech.forethought.stock.adapter.convertor.HSLTConvertor;
import tech.forethought.stock.adapter.convertor.HSZGConvertor;
import tech.forethought.stock.adapter.model.HSLT;
import tech.forethought.stock.entity.Industry;
import tech.forethought.stock.entity.Stock;
import tech.forethought.stock.entity.StockQuotationDaily;
import tech.forethought.stock.event.StockQuotationDailyEvent;
import tech.forethought.stock.repository.IndustryRepository;
import tech.forethought.stock.repository.StockQuotationDailyRepository;
import tech.forethought.stock.repository.StockRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StockQuotationDailyService {
    @Inject
    private StockQuotationDailyRepository stockQuotationDailyRepository;
    @Inject
    private StockRepository stockRepository;
    @Inject
    private IndustryRepository industryRepository;

    @NamiClient
    private HSLTClient hsltClient;
    @NamiClient
    private HSZBLClient hszblClient;
    @NamiClient
    private HSZBClient hszbClient;
    @NamiClient
    private HSZGClient hszgClient;

    @Inject("${mairui.licence}")
    private String licence;

    public void jobDaily() {
        log.info("StockJob start!");
        List<HSLT> hslts = hsltClient.list(licence);
        if (null == hslts || hslts.isEmpty()) {
            log.info("StockJob end! hslts is Empty!");
            return;
        }
        Set<String> codeSet = stockRepository.listAllCode();
        if (hslts.size() != codeSet.size()) {
            List<Stock> stocks = hslts.stream().filter(Objects::nonNull).filter(hslt -> !codeSet.contains(hslt.getDm()))
                    .map(HSLTConvertor::toStock).collect(Collectors.toList());
            if (!stocks.isEmpty()) {
                stockRepository.insertList(stocks);
                this.syncIndustry();
            }
        }
        EventBus.publishAsync(new StockQuotationDailyEvent());
        log.info("StockJob end!");
    }

    public void syncIndustry() {
        // 同步行业列表
        List<Industry> industryList = HSZGConvertor.toIndustry(hszgClient.list(licence).stream()
                .filter(hszg -> null != hszg.getType1() && null != hszg.getType2() && null != hszg.getLevel() && null != hszg.getIsleaf())
                .filter(hszg -> 0 == hszg.getType1() && 5 == hszg.getType2() && 3 == hszg.getLevel() && 1 == hszg.getIsleaf())
                .collect(Collectors.toList()));
        industryRepository.upsertList(industryList);
        // 同步股票所属行业
        industryList.forEach(industry -> {
            String code = industry.getCode();
            String name = industry.getName();
            log.info("sync industry {} {} start", code, name);
            try {
                List<HSLT> hsltList = hszgClient.gg("hangye_Z" + code, licence);
                if (null != hsltList) {
                    stockRepository.updateListById(hsltList.stream()
                            .filter(hslt -> "sh".equals(hslt.getJys()) || "sz".equals(hslt.getJys()))
                            .map(hslt -> {
                                Stock stock = HSLTConvertor.toStock(hslt);
                                stock.setIndustryCode(code);
                                stock.setIndustryName(name);
                                return stock;
                            }).collect(Collectors.toList()));
                }
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalStateException e) {
                log.warn("sync industry " + code + " " + name + " failed, stock not found!", e);
            }
            log.info("sync industry {} {} end", code, name);
        });
    }

    @Tran
    public void syncHistory(String code, String level, String licence) throws InterruptedException {
        // 同步股票历史日k
        long start = System.currentTimeMillis();
        Map<String, StockQuotationDaily> map = new HashMap<>();
        Optional.ofNullable(hszblClient.fsjy(code, level, licence)).ifPresent(list -> list.forEach(fsjy -> {
            StockQuotationDaily quotation = this.getStockQuotation(map, code, fsjy.getD());
            quotation.setOpeningPrice(fsjy.getO());
            quotation.setHighestPrice(fsjy.getH());
            quotation.setLowestPrice(fsjy.getL());
            quotation.setClosingPrice(fsjy.getC());
            quotation.setTradingVolume(fsjy.getV());
            quotation.setTransactionAmount(fsjy.getE());
            quotation.setAmplitude(fsjy.getZf());
            quotation.setTurnoverRate(fsjy.getHs());
            quotation.setPercentChange(fsjy.getZd());
            quotation.setPriceChange(fsjy.getZde());
        }));
        if (map.isEmpty()) {
            log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
            return;
        }
        stockQuotationDailyRepository.insertList(map.values().stream().toList());
//        map.values().forEach(quotation -> stockQuotationDailyRepository.upsertById(quotation));
        Stock stock = new Stock();
        stock.setCode(code);
        stockRepository.refreshSyncTime(stock);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

    private StockQuotationDaily getStockQuotation(Map<String, StockQuotationDaily> map,
                                                  String code,
                                                  String time) {
        String id = code + time.replaceAll("-", "");
        StockQuotationDaily quotation;
        if (map.containsKey(id)) {
            quotation = map.get(id);
        } else {
            quotation = new StockQuotationDaily();
            quotation.setId(id);
            quotation.setCode(code);
            quotation.setTradeDate(LocalDate.parse(time));
            map.put(id, quotation);
        }
        return quotation;
    }

    @Tran
    public void syncLatest(String code, String level, String licence) throws InterruptedException {
        // 同步股票历史日k
        long start = System.currentTimeMillis();
        StockQuotationDaily quotation = new StockQuotationDaily();
        quotation.setCode(code);
        Optional.ofNullable(hszbClient.fsjy(code, level, licence)).ifPresent(fsjy -> {
            String time = fsjy.getD();
            quotation.setId(code + time.replaceAll("-", ""));
            quotation.setTradeDate(LocalDate.parse(time));
            quotation.setOpeningPrice(fsjy.getO());
            quotation.setHighestPrice(fsjy.getH());
            quotation.setLowestPrice(fsjy.getL());
            quotation.setClosingPrice(fsjy.getC());
            quotation.setTradingVolume(fsjy.getV());
            quotation.setTransactionAmount(fsjy.getE());
            quotation.setAmplitude(fsjy.getZf());
            quotation.setTurnoverRate(fsjy.getHs());
            quotation.setPercentChange(fsjy.getZd());
            quotation.setPriceChange(fsjy.getZde());
        });
        if (Objects.isNull(quotation.getId())) {
            log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
            return;
        }
        stockQuotationDailyRepository.upsertById(quotation);
        Stock stock = new Stock();
        stock.setCode(code);
        stockRepository.refreshSyncTime(stock);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

}
