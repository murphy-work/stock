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

import java.time.LocalDateTime;

/**
 * 基金
 */
@Data
@Table("fund")
public class Fund {
    /**
     * 基金代码
     */
    @PrimaryKey
    private String code;
    /**
     * 基金名称
     */
    private String name;
    /**
     * 交易所
     */
    private String exchange;

    /**
     * 基金类型
     */
    private String type;

    /**
     * 基金每日行情同步时间
     */
    private LocalDateTime quotationDailySyncTime;
}
