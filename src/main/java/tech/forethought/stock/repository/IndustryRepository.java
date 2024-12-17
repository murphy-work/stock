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
import tech.forethought.stock.entity.Industry;

import java.util.List;

@Component
public class IndustryRepository {
    @Db
    private BaseMapper<Industry> industryMapper;

    public void insertList(List<Industry> industryList) {
        industryMapper.insertList(industryList);
    }

    public void upsertList(List<Industry> industryList) {
        industryList.forEach(industry -> industryMapper.upsert(industry, true));
    }

    public List<Industry> listRange(int start, int size) {
        return industryMapper.selectList(start, size, null);
    }
}
