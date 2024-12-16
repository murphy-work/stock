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

package tech.forethought.stock.entity;

import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 指数每日行情
 */
@Data
@Table("index_quotation_daily")
public class IndexQuotationDaily {
    /**
     * 编号 指数代码+交易时间 00000120240229
     */
    @PrimaryKey
    private String id;

    /**
     * 指数代码
     */
    private String code;
    /**
     * 交易时间
     */
    private LocalDate tradeDate;

    /**
     * 开盘价（元）
     */
    private BigDecimal openingPrice;
    /**
     * 最高价（元）
     */
    private BigDecimal highestPrice;
    /**
     * 最低价（元）
     */
    private BigDecimal lowestPrice;
    /**
     * 收盘价（元）
     */
    private BigDecimal closingPrice;
    /**
     * 成交量（手）
     */
    private Integer tradingVolume;
    /**
     * 振幅（%）
     */
    private BigDecimal amplitude;
    /**
     * 涨跌幅（%）
     */
    private BigDecimal percentChange;
    /**
     * 涨跌额（元）
     */
    private BigDecimal priceChange;

    /**
     * 成交额（元）
     */
    private BigDecimal transactionAmount;
    /**
     * 换手率（%）
     */
    private BigDecimal turnoverRate;
}
