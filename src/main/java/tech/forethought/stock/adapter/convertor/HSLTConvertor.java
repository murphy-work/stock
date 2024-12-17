/*
 * Copyright (c) [2024] [Murphy]
 * [Stock] is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan PubL v2.
 * You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 */

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
