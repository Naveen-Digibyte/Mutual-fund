package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.AMCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mutual-funds")
public class AMCSchemeController {
    
    @Autowired
    private AMCService amcService;
    
    @GetMapping()
    public ResponseEntity<ResponseModel> getFundDatas(){
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE,amcService.getAmcDetails(), null));
    }

    @GetMapping("/AMCs")
    public ResponseEntity<ResponseModel> getAllAMCs() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null,amcService.getAllAMCs()));
    }

    @GetMapping("/scheme-categories")
    public ResponseEntity<ResponseModel> getAllSchemeCategories() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getALlSchemeCategories()));
    }

    @GetMapping("/scheme-types")
    public ResponseEntity<ResponseModel> getAllSchemeTypes() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getAllSchemeTypes()));
    }

    @GetMapping("/funds/{amcId}")
    public ResponseEntity<ResponseModel> getFundsByAMCId(@PathVariable long amcId) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyId(amcId)));
    }

    @GetMapping("/fund/{amcId}/{code}")
    public ResponseEntity<ResponseModel> getFundByAMCIdAndCode(@PathVariable long amcId, @PathVariable String code) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundByAssetManagementCompanyIdAndCode(amcId, code)));
    }

    @GetMapping("/funds/{amcId}/{category}")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndSchemeCategory(@PathVariable long amcId, @PathVariable long category) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyIdAndSchemeCategory(amcId, category)));
    }

    @GetMapping("/funds/{amcId}/{type}")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndSchemeType(@PathVariable long amcId, @PathVariable long type) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyIdAndSchemeType(amcId, type)));
    }

    @GetMapping("/funds/{amcId}/fund-name")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndFundName(@PathVariable long amcId, @RequestParam String fundName) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyIdAndFundName(amcId, fundName)));
    }

    @GetMapping("/funds/{amcId}/launch-date")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndLaunchDate(@PathVariable long amcId, @RequestParam String launchDate) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyIdAndLaunchDate(amcId, launchDate)));
    }

    @GetMapping("/funds/{amcId}/isin-div-payout")
    public ResponseEntity<ResponseModel> getFundsByAMCIdAndIsinDivPayout(@PathVariable long amcId, @RequestParam String isinDivPayout) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByAssetManagementCompanyIdAndIsinDivPayout(amcId, isinDivPayout)));
    }

    @GetMapping("/no-closure-date")
    public ResponseEntity<ResponseModel> getFundsWithNoClosureDate() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getAllFundsWithNoClosureDate()));

    }

    @GetMapping("/pages")
    public ResponseEntity<ResponseModel> getFundsByPage(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, amcService.getFundsByPage(pageNumber, pageSize)));
    }

    @PostMapping
    public ResponseEntity<AssetManagementCompany> createAMC(@RequestBody AssetManagementCompany assetManagementCompany) {
        AssetManagementCompany createdAMC = amcService.createAMC(assetManagementCompany);
        return new ResponseEntity<>(createdAMC, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetManagementCompany> updateAMC(@PathVariable long id, @RequestBody AssetManagementCompany updatedAMC) {
        AssetManagementCompany amc = amcService.updateAMC(id, updatedAMC);
        return (amc != null) ? new ResponseEntity<>(amc, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAMC(@PathVariable long id) {
        amcService.deleteAMC(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
