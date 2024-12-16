package tech.forethought.stock.adapter.convertor;

import tech.forethought.stock.adapter.model.HSLT;
import tech.forethought.stock.entity.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class HSLTConvertor {
    public static Stock toStock(HSLT hslt) {
        Stock stock = new Stock();
        stock.setCode(hslt.getDm());
        stock.setName(hslt.getMc());
        stock.setExchange(hslt.getJys());
        return stock;
    }

    public static List<Stock> toStock(List<HSLT> hsltList) {
        return hsltList.stream().map(HSLTConvertor::toStock).collect(Collectors.toList());
    }
}
