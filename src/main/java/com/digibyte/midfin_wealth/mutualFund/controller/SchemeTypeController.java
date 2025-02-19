package com.digibyte.midfin_wealth.mutualFund.controller;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import com.digibyte.midfin_wealth.mutualFund.service.SchemeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -19-02-2025 <NaveenDhanasekaran> SchemeTypes
 * - InitialVersion
 */

@RestController
@RequestMapping("/api/schemeType")
@RequiredArgsConstructor
public class SchemeTypeController {

    private final SchemeTypeService schemeTypeService;

    @PostMapping
    public ResponseEntity<ResponseModel> createSchemeType(@RequestBody SchemeType schemeType) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, schemeTypeService.createSchemeType(schemeType)));
    }

    @GetMapping
    public ResponseEntity<ResponseModel> getAllSchemeTypes() {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, schemeTypeService.getAllSchemeTypes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getSchemeTypeById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, schemeTypeService.getSchemeTypeById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel> updateSchemeType(@PathVariable("id") long id,
                                                          @RequestBody SchemeType schemeTypeDetails) {
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, null, schemeTypeService.updateSchemeType(id, schemeTypeDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel> deleteSchemeType(@PathVariable("id") long id) {
        schemeTypeService.deleteSchemeType(id);
        return ResponseEntity.ok().body(new ResponseModel(Constants.POSITIVE, Constants.SCHEMETYPE_DELETED, null));
    }

}
