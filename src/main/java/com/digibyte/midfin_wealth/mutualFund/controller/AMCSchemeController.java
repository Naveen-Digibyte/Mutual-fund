package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.model.SchemeData;
import com.digibyte.midfin_wealth.mutualFund.service.AMCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mutual-funds")
public class AMCSchemeController {
    
    @Autowired
    private AMCService amcService;
    
    @GetMapping()
    public List<SchemeData> get(){
        return amcService.getAmcDetails();
    }
}
