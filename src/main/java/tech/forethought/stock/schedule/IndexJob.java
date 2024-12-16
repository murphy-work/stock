package tech.forethought.stock.schedule;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;
import tech.forethought.stock.service.IndexQuotationDailyService;

@Slf4j
@Scheduled(cron = "0 10 16 ? * 2-6")
public class IndexJob implements Runnable {
    @Inject
    private IndexQuotationDailyService indexQuotationDailyService;

    @Override
    public void run() {
        indexQuotationDailyService.jobDaily();
    }
}
