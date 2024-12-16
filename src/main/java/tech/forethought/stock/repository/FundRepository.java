package tech.forethought.stock.repository;

import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.WhereBase;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.Fund;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FundRepository {
    @Db
    private BaseMapper<Fund> fundMapper;

    public void insertList(List<Fund> fundList) {
        fundMapper.insertList(fundList);
    }

    public void refreshSyncTime(Fund fund) {
        fund.setName(null);
        fund.setExchange(null);
        fund.setType(null);
        fund.setQuotationDailySyncTime(LocalDateTime.now());
        fundMapper.updateById(fund, true);
    }

    public Set<String> listAllCode() {
        return fundMapper.selectArray("code", WhereBase::whereTrue)
                .stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
    }

    public long countAll() {
        return fundMapper.selectCount(mapperWhereQ -> mapperWhereQ.whereTrue().orderByAsc(Fund::getCode));
    }

    public List<Fund> listRange(int start, int size) {
        return fundMapper.selectList(start, size, mapperWhereQ -> mapperWhereQ.whereTrue().orderByAsc(Fund::getCode));
    }

}
