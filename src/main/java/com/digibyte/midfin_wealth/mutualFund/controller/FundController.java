package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> FundController
 *      - InitialVersion
 */

@RestController
@RequestMapping("/api/scheme")
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

    @GetMapping("/funds/{amcId}")
    public ResponseEntity<ResponseModel> getFundsByAMCId(@PathVariable long amcId) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyId(amcId)));
    }

    @GetMapping("/fund/{amcId}/{code}")
    public ResponseEntity<ResponseModel> getFundByAMCIdAndCode(@PathVariable long amcId, @PathVariable String code) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundByAssetManagementCompanyIdAndCode(amcId, code)));
    }

    @GetMapping("/funds/{amcId}/{category}")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndSchemeCategory(@PathVariable long amcId, @PathVariable long category) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyIdAndSchemeCategory(amcId, category)));
    }

    @GetMapping("/funds/{amcId}/{type}")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndSchemeType(@PathVariable long amcId, @PathVariable long type) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyIdAndSchemeType(amcId, type)));
    }

    @GetMapping("/funds/{amcId}/fund-name")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndFundName(@PathVariable long amcId, @RequestParam String fundName) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyIdAndFundName(amcId, fundName)));
    }

    @GetMapping("/funds/{amcId}/launch-date")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndLaunchDate(@PathVariable long amcId, @RequestParam String launchDate) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyIdAndLaunchDate(amcId, launchDate)));
    }

    @GetMapping("/funds/{amcId}/isin-div-payout")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndIsinDivPayout(@PathVariable long amcId, @RequestParam String isinDivPayout) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByAssetManagementCompanyIdAndIsinDivPayout(amcId, isinDivPayout)));
    }

    @GetMapping("/no-closure-date")
    public ResponseEntity<ResponseModel> getFundsWithNoClosureDate() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getAllFundsWithNoClosureDate()));

    }

    @GetMapping("/pages")
    public ResponseEntity<ResponseModel> getFundsByPage(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, fundService.getFundsByPage(pageNumber, pageSize)));
    }

}
