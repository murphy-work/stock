package tech.forethought.stock.repository;

import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.WhereBase;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.Stock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StockRepository {
    @Db
    private BaseMapper<Stock> stockMapper;

    public void insertList(List<Stock> stockList) {
        stockMapper.insertList(stockList);
    }

    public void refreshSyncTime(Stock stock) {
        stock.setName(null);
        stock.setExchange(null);
        stock.setIndustryCode(null);
        stock.setIndustryName(null);
        stock.setQuotationDailySyncTime(LocalDateTime.now());
        stockMapper.updateById(stock, true);
    }

    public void updateListById(List<Stock> stockList) {
        stockMapper.updateList(stockList, (stock, dataItem) -> dataItem.setEntity(stock), Stock::getCode);
    }

    public Set<String> listAllCode() {
        return stockMapper.selectArray("code", WhereBase::whereTrue)
                .stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
    }

    public List<Stock> listAll() {
        return stockMapper.selectList(mapperWhereQ -> mapperWhereQ.whereNeq(Stock::getIndustryCode, null).orderByAsc(Stock::getCode));
    }

    public long countAll() {
        return stockMapper.selectCount(mapperWhereQ -> mapperWhereQ.whereNeq(Stock::getIndustryCode, null).orderByAsc(Stock::getCode));
    }

    public List<Stock> listRange(int start, int size) {
        return stockMapper.selectList(start, size, mapperWhereQ -> mapperWhereQ.whereNeq(Stock::getIndustryCode, null).orderByAsc(Stock::getCode));
    }

}
