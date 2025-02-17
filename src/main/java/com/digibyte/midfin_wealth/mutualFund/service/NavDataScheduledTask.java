package com.digibyte.midfin_wealth.mutualFund.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NavDataScheduledTask {
    private final NavService navService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void getDailyNav() throws Exception {
        navService.fetchAndSaveDataFromApi();
    }
}
