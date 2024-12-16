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

package tech.forethought.stock.adapter.client;

import org.noear.nami.annotation.NamiClient;
import org.noear.nami.annotation.NamiMapping;
import tech.forethought.stock.adapter.model.FSJY;
import tech.forethought.stock.adapter.model.JJ;

import java.util.List;

@NamiClient(url = "http://api.mairui.club/jj")
public interface JJClient {
    @NamiMapping("GET all/{licence}")
    List<JJ> all(String licence);

    @NamiMapping("GET fbs/{licence}")
    List<JJ> fbs(String licence);

    @NamiMapping("GET etf/{licence}")
    List<JJ> etf(String licence);

    @NamiMapping("GET lof/{licence}")
    List<JJ> lof(String licence);

    @NamiMapping("GET lskx/{code}/{level}/{licence}")
    List<FSJY> lskx(String code, String level, String licence);

    @NamiMapping("GET zxkx/{code}/{level}/{licence}")
    FSJY zxkx(String code, String level, String licence);
}
