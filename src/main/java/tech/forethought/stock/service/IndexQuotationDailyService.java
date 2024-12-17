package tech.forethought.stock.service;

import lombok.extern.slf4j.Slf4j;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.data.annotation.Tran;
import tech.forethought.stock.adapter.client.ZSClient;
import tech.forethought.stock.adapter.convertor.ZSConvertor;
import tech.forethought.stock.adapter.model.ZS;
import tech.forethought.stock.entity.Index;
import tech.forethought.stock.entity.IndexQuotationDaily;
import tech.forethought.stock.event.IndexQuotationDailyEvent;
import tech.forethought.stock.repository.IndexQuotationDailyRepository;
import tech.forethought.stock.repository.IndexRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexQuotationDailyService {
    @Inject
    private IndexQuotationDailyRepository indexQuotationDailyRepository;
    @Inject
    private IndexRepository indexRepository;

    @NamiClient
    private ZSClient zsClient;

    @Inject("${mairui.licence}")
    private String licence;

    public void jobDaily() {
        log.info("IndexJob start!");
        List<ZS> zs = zsClient.all(licence);
        if (null == zs || zs.isEmpty()) {
            log.info("IndexJob end! jjs is Empty!");
            return;
        }
        Set<String> codeSet = indexRepository.listAllCode();
        if (zs.size() != codeSet.size()) {
            List<Index> indices = zs.stream().filter(Objects::nonNull).map(ZSConvertor::toIndex)
                    .filter(index -> !codeSet.contains(index.getCode())).collect(Collectors.toList());
            if (!indices.isEmpty()) {
                indexRepository.insertList(indices);
            }
        }
        EventBus.publishAsync(new IndexQuotationDailyEvent());
        log.info("IndexJob end!");
    }

    @Tran
    public void syncHistory(String code, String exchange, String level, String licence) throws InterruptedException {
        // 同步指数历史日k
        long start = System.currentTimeMillis();
        Map<String, IndexQuotationDaily> map = new HashMap<>();
        Optional.ofNullable(zsClient.hfsjy(exchange + code, level, licence)).ifPresent(list -> list.forEach(fsjy -> {
            IndexQuotationDaily quotation = this.getIndexQuotation(map, code, fsjy.getD());
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
        indexQuotationDailyRepository.insertList(map.values().stream().toList());
        Index index = new Index();
        index.setCode(code);
        indexRepository.refreshSyncTime(index);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

    private IndexQuotationDaily getIndexQuotation(Map<String, IndexQuotationDaily> map,
                                                  String code,
                                                  String time) {
        String id = code + time.replaceAll("-", "");
        IndexQuotationDaily quotation;
        if (map.containsKey(id)) {
            quotation = map.get(id);
        } else {
            quotation = new IndexQuotationDaily();
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
        IndexQuotationDaily quotation = new IndexQuotationDaily();
        quotation.setCode(code);
        Optional.ofNullable(zsClient.fsjy(exchange + code, level, licence)).ifPresent(fsjy -> {
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
        indexQuotationDailyRepository.upsertById(quotation);
        Index index = new Index();
        index.setCode(code);
        indexRepository.refreshSyncTime(index);
        log.info(code + " cost: " + (System.currentTimeMillis() - start) + " ms");
    }

}
