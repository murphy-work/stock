package tech.forethought.stock.schedule;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;
import tech.forethought.stock.service.StockQuotationDailyService;

@Slf4j
@Scheduled(cron = "0 10 18 ? * 2-6")
public class StockJob implements Runnable {
    @Inject
    private StockQuotationDailyService stockQuotationDailyService;

    @Override
    public void run() {
        stockQuotationDailyService.jobDaily();
    }
}
