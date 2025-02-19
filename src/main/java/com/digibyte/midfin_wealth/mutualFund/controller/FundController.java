package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
public class FundController {
    
    private final FundService fundService;
    
    @PostMapping
    public ResponseEntity<ResponseModel> saveFund(@RequestBody FundRequestModel requestModel){
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE,fundService.createFund(requestModel), null));
    }
    
    @GetMapping("/{fundId}")
    public ResponseEntity<ResponseModel> getFundById(@PathVariable(name = "fundId") long id){
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE,null , fundService.getfundById(id)));
    }
}
