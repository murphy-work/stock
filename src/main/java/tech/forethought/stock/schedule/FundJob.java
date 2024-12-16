package tech.forethought.stock.schedule;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;
import tech.forethought.stock.service.FundQuotationDailyService;

@Slf4j
@Scheduled(cron = "0 10 17 ? * 2-6")
public class FundJob implements Runnable {
    @Inject
    private FundQuotationDailyService fundQuotationDailyService;

    @Override
    public void run() {
        fundQuotationDailyService.jobDaily();
    }
}
