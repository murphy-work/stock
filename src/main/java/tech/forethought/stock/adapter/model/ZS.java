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

package tech.forethought.stock.adapter.model;

import lombok.Data;

/**
 * 沪深主要指数
 */
@Data
public class ZS {
    /**
     * 指数代码，如：sh000001（注意，股票代码可以不带“sh/sz”前缀，指数代码必须带）
     */
    private String dm;
    /**
     * 股票名称，例如：工商银行
     */
    private String mc;
    /**
     * 交易所，"sh"表示上证，"sz"表示深证
     */
    private String jys;
}
