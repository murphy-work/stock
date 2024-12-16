package tech.forethought.stock.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventListener;
import tech.forethought.stock.event.IndexQuotationDailyEvent;
import tech.forethought.stock.repository.IndexRepository;
import tech.forethought.stock.service.IndexQuotationDailyService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class IndexQuotationDailyEventListener implements EventListener<IndexQuotationDailyEvent> {
    @Inject
    private IndexRepository indexRepository;
    @Inject
    private IndexQuotationDailyService indexQuotationDailyService;

    @Inject("${mairui.licence}")
    private String licence;
    private final String level = "dn";

    @Override
    public void onEvent(IndexQuotationDailyEvent event) {
        long indexNum = indexRepository.countAll();
        int start = 0;
        int size = 2;
        AtomicInteger current = new AtomicInteger(start);
        LocalDateTime startTime = LocalDateTime.now();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (indexNum <= current.get()) {
                    timer.cancel();
                    log.info("IndexQuotationDailyEvent finished, cost: {}", ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()));
                    return;
                }
                log.info("IndexQuotationDailyEvent {} -> {}, start", current.get(), current.get() + size - 1);
                LocalDateTime eventStartTime = LocalDateTime.now();
                indexRepository.listRange(current.get(), size).parallelStream().forEach(index -> {
                    try {
                        indexQuotationDailyService.syncLatest(index.getCode(), index.getExchange(), level, licence);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                LocalDateTime eventEndTime = LocalDateTime.now();
                log.info("IndexQuotationDailyEvent {} -> {}, cost: {}s", current.get(), current.get() + size - 1,
                        ChronoUnit.SECONDS.between(eventStartTime, eventEndTime));
                current.getAndAdd(size);
            }
        }, 0, 5 * 1000);
    }

}
