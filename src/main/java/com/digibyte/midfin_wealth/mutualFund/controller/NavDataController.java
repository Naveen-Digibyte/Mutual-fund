package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.NavService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/nav")
@RequiredArgsConstructor
public class NavDataController {

    private final NavService navService;

    @GetMapping
    public void getDailyNav() throws Exception {
        navService.fetchAndSaveDataFromApi();
    }

    @PostMapping("/fund/{schemeCode}")
    public ResponseEntity<ResponseModel> getNavForScheme(
            @PathVariable(name = "schemeCode") String schemeCode,
            @RequestParam(name = "amcId") String amcId,
            @RequestParam(name = "fromDate") String fromDateStr,
            @RequestParam(name = "toDate") String toDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        LocalDate fromDate = LocalDate.parse(fromDateStr, formatter);
        LocalDate toDate = LocalDate.parse(toDateStr, formatter);
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, navService.getSaveNav(schemeCode, amcId, fromDate, toDate), null));
    }

    @GetMapping("/fund/{fundId}")
    public ResponseEntity<ResponseModel> getByFUndId(@PathVariable(name = "fundId") long fundId) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, navService.getNavByFundCode(fundId)));
    }
}
