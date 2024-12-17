package tech.forethought.stock.service;

import lombok.extern.slf4j.Slf4j;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.data.annotation.Tran;
import tech.forethought.stock.adapter.client.JJClient;
import tech.forethought.stock.adapter.convertor.JJConvertor;
import tech.forethought.stock.adapter.model.JJ;
import tech.forethought.stock.entity.Fund;
import tech.forethought.stock.entity.FundQuotationDaily;
import tech.forethought.stock.event.FundQuotationDailyEvent;
import tech.forethought.stock.repository.FundQuotationDailyRepository;
import tech.forethought.stock.repository.FundRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FundQuotationDailyService {
    @Inject
    private FundQuotationDailyRepository fundQuotationDailyRepository;
    @Inject
    private FundRepository fundRepository;

    @NamiClient
    private JJClient jjClient;

    @Inject("${mairui.licence}")
    private String licence;

    public void jobDaily() {
        log.info("FundJob start!");
        List<JJ> jjs = jjClient.etf(licence);
        if (null == jjs || jjs.isEmpty()) {
            log.info("FundJob end! jjs is Empty!");
            return;
        }
        Set<String> codeSet = fundRepository.listAllCode();
        if (jjs.size() != codeSet.size()) {
            List<Fund> funds = jjs.stream().filter(Objects::nonNull).map(JJConvertor::toEtf).filter(etf -> !codeSet.contains(etf.getCode()))
                    .collect(Collectors.toList());
            if (!funds.isEmpty()) {
                fundRepository.insertList(funds);
            }
        }
        EventBus.publishAsync(new FundQuotationDailyEvent());
        log.info("FundJob end!");
    }

    @Tran
    public void syncHistory(String code, String exchange, String level, String licence) throws InterruptedException {
        // 同步基金历史日k
        long start = System.currentTimeMillis();
        Map<String, FundQuotationDaily> map = new HashMap<>();
        Optional.ofNullable(jjClient.lskx(exchange + code, level, licence)).ifPresent(list -> list.forEach(fsjy -> {
            FundQuotationDaily quotation = this.getFundQuotation(map, code, fsjy.getD());
            quotation.setOpeningPrice(fsjy.getO());
            quotation.setHighestPrice(fsjy.getH());
            quotation.setLowestPrice(fsjy.getL());
            quotation.setClosingPrice(fsjy.getC());
            quotation.setTradingVolume(fsjy.getV());
            quotation.setAmplitude(fsjy.getZf());
            quotation.setPercentChange(fsjy.getZd());
            quotation.setPriceChange(fsjy.getZde());
        }));
        if (map.isEmpty()) {
            log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
            return;
        }
        fundQuotationDailyRepository.insertList(map.values().stream().toList());
        Fund fund = new Fund();
        fund.setCode(code);
        fundRepository.refreshSyncTime(fund);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

    private FundQuotationDaily getFundQuotation(Map<String, FundQuotationDaily> map,
                                                String code,
                                                String time) {
        String id = code + time.replaceAll("-", "");
        FundQuotationDaily quotation;
        if (map.containsKey(id)) {
            quotation = map.get(id);
        } else {
            quotation = new FundQuotationDaily();
            quotation.setId(id);
            quotation.setCode(code);
            quotation.setTradeDate(LocalDate.parse(time));
            map.put(id, quotation);
        }
        return quotation;
    }

    @Tran
    public void syncLatest(String code, String exchange, String level, String licence) throws InterruptedException {
        // 同步股票历史日k
        long start = System.currentTimeMillis();
        FundQuotationDaily quotation = new FundQuotationDaily();
        quotation.setCode(code);
        Optional.ofNullable(jjClient.zxkx(exchange + code, level, licence)).ifPresent(fsjy -> {
            String time = fsjy.getD();
            quotation.setId(code + time.replaceAll("-", ""));
            quotation.setTradeDate(LocalDate.parse(time));
            quotation.setOpeningPrice(fsjy.getO());
            quotation.setHighestPrice(fsjy.getH());
            quotation.setLowestPrice(fsjy.getL());
            quotation.setClosingPrice(fsjy.getC());
            quotation.setTradingVolume(fsjy.getV());
            quotation.setAmplitude(fsjy.getZf());
            quotation.setPercentChange(fsjy.getZd());
            quotation.setPriceChange(fsjy.getZde());
        });
        if (Objects.isNull(quotation.getId())) {
            log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
            return;
        }
        fundQuotationDailyRepository.upsertById(quotation);
        Fund fund = new Fund();
        fund.setCode(code);
        fundRepository.refreshSyncTime(fund);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

}
