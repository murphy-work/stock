package tech.forethought.stock.adapter.convertor;

import tech.forethought.stock.adapter.model.HSZG;
import tech.forethought.stock.entity.Industry;

import java.util.List;
import java.util.stream.Collectors;

public class HSZGConvertor {
    public static Industry toIndustry(HSZG hslt) {
        Industry industry = new Industry();
        industry.setCode(hslt.getCode().substring("hangye_Z".length()));
        industry.setName(hslt.getName().substring(hslt.getName().lastIndexOf("-") + 1));
        industry.setCodeParent(hslt.getPcode().substring("hangye_Z".length()));
        industry.setNameParent(hslt.getPname().substring(hslt.getPname().lastIndexOf("-") + 1));
        return industry;
    }

    public static List<Industry> toIndustry(List<HSZG> hsltList) {
        return hsltList.stream().map(HSZGConvertor::toIndustry).collect(Collectors.toList());
    }
}
