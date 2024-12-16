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
import tech.forethought.stock.adapter.convertor.HSLTConvertor;
import tech.forethought.stock.adapter.model.HSLT;
import tech.forethought.stock.entity.Stock;
import tech.forethought.stock.entity.StockQuotationDaily;
import tech.forethought.stock.event.StockQuotationDailyEvent;
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

    @NamiClient
    private HSLTClient hsltClient;
    @NamiClient
    private HSZBLClient hszblClient;
    @NamiClient
    private HSZBClient hszbClient;

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
            }
        }
        EventBus.publishAsync(new StockQuotationDailyEvent());
        log.info("StockJob end!");
    }

    @Tran
    public void syncHistory(String code, String level, String licence) throws InterruptedException {
        // 同步股票历史日k
        long start = System.currentTimeMillis();
        Map<String, StockQuotationDaily> map = new HashMap<>();
        Optional.ofNullable(hszblClient.fsjy(code, level, licence)).ifPresent(list -> list.forEach(fsjy -> {
//            if (!LocalDate.parse(fsjy.getD()).isEqual(LocalDate.parse("2024-04-08")) &&
//                    !LocalDate.parse(fsjy.getD()).isAfter(LocalDate.parse("2024-04-08"))) {
//                return;
//            }
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
//            log.info(code + " cost0: " + (System.currentTimeMillis() - start) + " ms");
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
//        stock.setOpeningPrice(quotation.getOpeningPrice());
//        stock.setHighestPrice(quotation.getHighestPrice());
//        stock.setLowestPrice(quotation.getLowestPrice());
//        stock.setClosingPrice(quotation.getClosingPrice());
//        stock.setTradingVolume(quotation.getTradingVolume());
//        stock.setTransactionAmount(quotation.getTransactionAmount());
//        stock.setAmplitude(quotation.getAmplitude());
//        stock.setTurnoverRate(quotation.getTurnoverRate());
//        stock.setPercentChange(quotation.getPercentChange());
//        stock.setPriceChange(quotation.getPriceChange());
        stockRepository.refreshSyncTime(stock);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

}
