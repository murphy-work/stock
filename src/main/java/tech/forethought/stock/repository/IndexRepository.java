package tech.forethought.stock.repository;

import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.WhereBase;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.Index;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class IndexRepository {
    @Db
    private BaseMapper<Index> indexMapper;

    public void insertList(List<Index> indexList) {
        indexMapper.insertList(indexList);
    }

    public void refreshSyncTime(Index index) {
        index.setName(null);
        index.setExchange(null);
        index.setQuotationDailySyncTime(LocalDateTime.now());
        indexMapper.updateById(index, true);
    }

    public Set<String> listAllCode() {
        return indexMapper.selectArray("code", WhereBase::whereTrue)
                .stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
    }

    public long countAll() {
        return indexMapper.selectCount(mapperWhereQ -> mapperWhereQ.whereTrue().orderByAsc(Index::getCode));
    }

    public List<Index> listRange(int start, int size) {
        return indexMapper.selectList(start, size, mapperWhereQ -> mapperWhereQ.whereTrue().orderByAsc(Index::getCode));
    }
}
