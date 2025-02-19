package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.AMCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> AMCSchemeController
 *      - InitialVersion
 */

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
