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
