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
