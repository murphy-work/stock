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

package tech.forethought.stock.repository;

import org.noear.solon.annotation.Component;
import org.noear.wood.BaseMapper;
import org.noear.wood.annotation.Db;
import tech.forethought.stock.entity.FundQuotationDaily;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FundQuotationDailyRepository {
    @Db
    private BaseMapper<FundQuotationDaily> fundQuotationDailyMapper;

    public void insertList(List<FundQuotationDaily> fundQuotationDailyList) {
        fundQuotationDailyMapper.insertList(fundQuotationDailyList);
    }

    public void upsertById(FundQuotationDaily fundQuotationDaily) {
        fundQuotationDailyMapper.upsert(fundQuotationDaily, true);
    }

    public void updateListById(List<FundQuotationDaily> fundQuotationDailyList) {
        fundQuotationDailyMapper.updateList(fundQuotationDailyList, (fundQuotationDaily, dataItem) -> dataItem.setEntity(fundQuotationDaily), FundQuotationDaily::getId);
    }

    public Map<String, List<FundQuotationDaily>> mapAll() {
        return fundQuotationDailyMapper.selectList(mapperWhereQ -> mapperWhereQ.whereTrue().orderByAsc(FundQuotationDaily::getId))
                .stream().collect(Collectors.groupingBy(FundQuotationDaily::getCode));
    }

    public FundQuotationDaily findById(String id) {
        return fundQuotationDailyMapper.selectById(id);
    }
}
