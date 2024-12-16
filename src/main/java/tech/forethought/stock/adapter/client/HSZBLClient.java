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

import java.util.List;

@NamiClient(url = "http://api.mairui.club/hszbl")
public interface HSZBLClient {
    @NamiMapping("GET fsjy/{code}/{level}/{licence}")
    List<FSJY> fsjy(String code, String level, String licence);
}
