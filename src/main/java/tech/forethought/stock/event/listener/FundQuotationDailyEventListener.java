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

package tech.forethought.stock.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventListener;
import tech.forethought.stock.event.FundQuotationDailyEvent;
import tech.forethought.stock.repository.FundRepository;
import tech.forethought.stock.service.FundQuotationDailyService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class FundQuotationDailyEventListener implements EventListener<FundQuotationDailyEvent> {
    @Inject
    private FundRepository fundRepository;
    @Inject
    private FundQuotationDailyService fundQuotationDailyService;

    @Inject("${mairui.licence}")
    private String licence;
    private final String level = "dn";

    @Override
    public void onEvent(FundQuotationDailyEvent event) {
        long fundNum = fundRepository.countAll();
        int start = 0;
        int size = 5;
        AtomicInteger current = new AtomicInteger(start);
        LocalDateTime startTime = LocalDateTime.now();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (fundNum <= current.get()) {
                    timer.cancel();
                    log.info("FundQuotationDailyEvent finished, cost: {}s", ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()));
                    return;
                }
                log.info("FundQuotationDailyEvent {} -> {}, start", current.get(), current.get() + size - 1);
                LocalDateTime eventStartTime = LocalDateTime.now();
                fundRepository.listRange(current.get(), size).parallelStream().forEach(fund -> {
                    try {
                        fundQuotationDailyService.syncLatest(fund.getCode(), fund.getExchange(), level, licence);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                LocalDateTime eventEndTime = LocalDateTime.now();
                log.info("FundQuotationDailyEvent {} -> {}, cost: {}s", current.get(), current.get() + size - 1,
                        ChronoUnit.SECONDS.between(eventStartTime, eventEndTime));
                current.getAndAdd(size);
            }
        }, 0, 3 * 1000);
    }

}
