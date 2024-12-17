package tech.forethought.stock.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.EventListener;
import tech.forethought.stock.event.StockQuotationDailyEvent;
import tech.forethought.stock.repository.StockRepository;
import tech.forethought.stock.service.StockQuotationDailyService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class StockQuotationDailyEventListener implements EventListener<StockQuotationDailyEvent> {
    @Inject
    private StockRepository stockRepository;
    @Inject
    private StockQuotationDailyService stockQuotationDailyService;

    @Inject("${mairui.licence}")
    private String licence;
    private final String level = "dq";

    @Override
    public void onEvent(StockQuotationDailyEvent event) {
        long stockNum = stockRepository.countAll();
        int start = 0;
        int size = 5;
        AtomicInteger current = new AtomicInteger(start);
        LocalDateTime startTime = LocalDateTime.now();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (stockNum <= current.get()) {
                    timer.cancel();
                    log.info("StockQuotationDailyEvent finished, cost: {}s", ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()));
                    return;
                }
                log.info("StockQuotationDailyEvent {} -> {}, start", current.get(), current.get() + size - 1);
                LocalDateTime eventStartTime = LocalDateTime.now();
                stockRepository.listRange(current.get(), size).parallelStream().forEach(stock -> {
                    try {
                        stockQuotationDailyService.syncHistory(stock.getCode(), level, licence);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                LocalDateTime eventEndTime = LocalDateTime.now();
                log.info("StockQuotationDailyEvent {} -> {}, cost: {}s", current.get(), current.get() + size - 1,
                        ChronoUnit.SECONDS.between(eventStartTime, eventEndTime));
                current.getAndAdd(size);
            }
        }, 0, 3 * 1000);
    }

}
