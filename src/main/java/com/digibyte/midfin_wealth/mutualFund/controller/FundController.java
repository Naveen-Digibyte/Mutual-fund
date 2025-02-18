package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
public class FundController {
    
    private final FundService fundService;
    
    @PostMapping
    public String saveFund(@RequestBody FundRequestModel requestModel){
        return fundService.createFund(requestModel);
    }
}
