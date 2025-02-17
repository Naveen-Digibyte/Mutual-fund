package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.service.NavService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nav")
@RequiredArgsConstructor
public class NavDataController {
    
    private final NavService navService;
    
    @GetMapping
    public void getDailyNav() throws Exception {
        navService.fetchAndSaveDataFromApi();
    }
}
