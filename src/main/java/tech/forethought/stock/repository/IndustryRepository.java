package tech.forethought.stock.repository;

import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.Industry;

import java.util.List;

@Component
public class IndustryRepository {
    @Db
    private BaseMapper<Industry> industryMapper;

    public void insertList(List<Industry> industryList) {
        industryMapper.insertList(industryList);
    }

    public List<Industry> listRange(int start, int size) {
        return industryMapper.selectList(start, size, null);
    }
}
