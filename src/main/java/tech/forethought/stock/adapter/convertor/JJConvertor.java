package tech.forethought.stock.adapter.convertor;

import tech.forethought.stock.adapter.model.JJ;
import tech.forethought.stock.entity.Fund;

import java.util.List;
import java.util.stream.Collectors;

public class JJConvertor {
    public static Fund toEtf(JJ jj) {
        String dm = jj.getDm();
        Fund etf = new Fund();
        etf.setCode(dm.substring(2));
        etf.setName(jj.getMc());
        etf.setExchange(dm.substring(0, 2));
        etf.setType("etf");
        return etf;
    }

    public static List<Fund> toEtf(List<JJ> jjList) {
        return jjList.stream().map(JJConvertor::toEtf).collect(Collectors.toList());
    }
}
