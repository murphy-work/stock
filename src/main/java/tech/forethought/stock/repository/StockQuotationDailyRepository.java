package tech.forethought.stock.repository;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.StockQuotationDaily;

import java.util.List;

@Component
@Slf4j
public class StockQuotationDailyRepository {
    @Db
    private BaseMapper<StockQuotationDaily> stockQuotationDailyMapper;

    public void insertList(List<StockQuotationDaily> stockQuotationDailyList) {
        stockQuotationDailyMapper.insertList(stockQuotationDailyList);
    }

    public void upsertById(StockQuotationDaily stockQuotationDaily) {
        stockQuotationDailyMapper.upsert(stockQuotationDaily, true);
    }

    public void updateListById(List<StockQuotationDaily> stockQuotationDailyList) {
        stockQuotationDailyMapper.updateList(stockQuotationDailyList, (stockQuotationDaily, dataItem) -> dataItem.setEntity(stockQuotationDaily), StockQuotationDaily::getId);
    }

    public StockQuotationDaily findById(String id) {
        return stockQuotationDailyMapper.selectById(id);
    }
}
