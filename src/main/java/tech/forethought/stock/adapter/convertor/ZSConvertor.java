package tech.forethought.stock.adapter.convertor;

import tech.forethought.stock.adapter.model.ZS;
import tech.forethought.stock.entity.Index;

import java.util.List;
import java.util.stream.Collectors;

public class ZSConvertor {
    public static Index toIndex(ZS zs) {
        Index index = new Index();
        index.setCode(zs.getDm().substring(zs.getJys().length()));
        index.setName(zs.getMc());
        index.setExchange(zs.getJys());
        return index;
    }

    public static List<Index> toIndex(List<ZS> zsList) {
        return zsList.stream().map(ZSConvertor::toIndex).collect(Collectors.toList());
    }
}
