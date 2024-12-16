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

import java.math.BigDecimal;

@Data
public class FSJY {
    /**
     * 交易时间，短分时级别格式为yyyy-MM-dd HH:mm:ss，日线级别为yyyy-MM-dd
     */
    private String d;
    /**
     * 开盘价（元）
     */
    private BigDecimal o;
    /**
     * 最高价（元）
     */
    private BigDecimal h;
    /**
     * 最低价（元）
     */
    private BigDecimal l;
    /**
     * 收盘价（元）
     */
    private BigDecimal c;
    /**
     * 成交量（手）
     */
    private Integer v;
    /**
     * 成交额（元）
     */
    private BigDecimal e;
    /**
     * 振幅（%）
     */
    private BigDecimal zf;
    /**
     * 换手率（%）
     */
    private BigDecimal hs;
    /**
     * 涨跌幅（%）
     */
    private BigDecimal zd;
    /**
     * 涨跌额（元）
     */
    private BigDecimal zde;
}
