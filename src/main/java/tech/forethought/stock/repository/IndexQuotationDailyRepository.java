package tech.forethought.stock.repository;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.IndexQuotationDaily;

import java.util.List;

@Component
@Slf4j
public class IndexQuotationDailyRepository {
    @Db
    private BaseMapper<IndexQuotationDaily> IndexQuotationDailyMapper;

    public void insertList(List<IndexQuotationDaily> IndexQuotationDailyList) {
        IndexQuotationDailyMapper.insertList(IndexQuotationDailyList);
    }

    public void upsertById(IndexQuotationDaily IndexQuotationDaily) {
        IndexQuotationDailyMapper.upsert(IndexQuotationDaily, true);
    }

    public void updateListById(List<IndexQuotationDaily> IndexQuotationDailyList) {
        IndexQuotationDailyMapper.updateList(IndexQuotationDailyList, (IndexQuotationDaily, dataItem) -> dataItem.setEntity(IndexQuotationDaily), IndexQuotationDaily::getId);
    }

    public IndexQuotationDaily findById(String id) {
        return IndexQuotationDailyMapper.selectById(id);
    }
}
