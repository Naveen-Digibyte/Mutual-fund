package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.*;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.repository.*;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> AMCService
 *      - InitialVersion
 */

@Service
@RequiredArgsConstructor
public class AMCService {

    private final AMCRepository amcRepository;
    private final RestTemplate restTemplate;
    private final SchemeCategoryRepository schemeRepository;
    private final AMCFundsRepository amcFundsRepository;
    private final SchemeTypeRepository schemeTypeRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Value("${amfiAPI.schemeData}")
    private String SchemeDataUrl;

    @Value("minio.bucketName")
    private String bucketName;


    public String getAmcDetails() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(SchemeDataUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                parseCsv(response.getBody());
                return Constants.DATA_PROCESSED_SUCCESSFUL;
            } else {
                throw new FundException(String.format(ErrorConstants.E_001, response.getStatusCode()));
            }
        } catch (Exception e) {
            throw new FundException(String.format(ErrorConstants.E_002, e.getMessage()));
        }
    }

    private void parseCsv(String csvContent) {
        try (StringReader reader = new StringReader(csvContent)) {
            CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                processRecord(record);
            }
        } catch (Exception e) {
            throw new FundException(e.getMessage());
        }
    }

    private void processRecord(CSVRecord record) {
        AssetManagementCompany amc = getOrCreateAMC(record.get("AMC"));
        String schemeCategory = null;
        String schemeSubCategory = null;
        if (record.get("Scheme Category").contains("-")) {
            schemeCategory = record.get("Scheme Category").split("-")[0].trim();
            schemeSubCategory = record.get("Scheme Category").split("-")[1].trim();
        } else {
            schemeSubCategory = record.get("Scheme Category").trim();
        }

        SchemeType schemeType = getOrCreateSchemeType(record.get("Scheme Type"));
        String isin = record.get("ISIN Div Payout/ ISIN GrowthISIN Div Reinvestment");
        String[] isinParts = parseIsin(isin);
        createOrUpdateFund(record, amc, getOrCreateSchemeCategory(schemeCategory), schemeType, isinParts, getOrCreateSchemeSubCategory(schemeSubCategory));
    }

    private AssetManagementCompany getOrCreateAMC(String amcName) {
        return amcRepository.findByName(amcName).orElseGet(() -> amcRepository.save(AssetManagementCompany.builder().name(amcName).status(Status.INACTIVE).build()));
    }

    private SchemeCategory getOrCreateSchemeCategory(String category) {
        return schemeRepository.findByCategory(category).orElseGet(() -> schemeRepository.save(SchemeCategory.builder().category(category).build()));
    }

    private SchemeType getOrCreateSchemeType(String type) {
        return schemeTypeRepository.findByType(type).orElseGet(() -> schemeTypeRepository.save(SchemeType.builder().type(type).build()));
    }

    private SchemeSubCategory getOrCreateSchemeSubCategory(String subCategory) {
        return subCategoryRepository.findBySubCategory(subCategory).orElseGet(() -> subCategoryRepository.save(SchemeSubCategory.builder().subCategory(subCategory).build()));
    }
    
    private LocalDate getLaunchDate(String date){
        if(date.isEmpty()){
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(date.length() == 9 ? "dd-MMM-yy" : "dd-MMM-yyyy", Locale.ENGLISH));
    }

    private String[] parseIsin(String isin) {
        if (isin == null || isin.isBlank()) {
            return new String[]{null, null, null};
        }
        String[] val = isin.split("IN");
        String payout = val.length > 1 ? "IN" + val[1] : null;
        String reinvestment = val.length > 2 ? "IN" + val[2] : null;
        return new String[]{payout, reinvestment};
    }

    private void createOrUpdateFund(CSVRecord record, AssetManagementCompany amc, SchemeCategory schemeCategory, SchemeType schemeType, String[] isinParts, SchemeSubCategory schemeSubCategory) {
       if(!amcFundsRepository.existsByCode(record.get("Code"))){
        amcFundsRepository.save(AMCFund.builder()
                .assetManagementCompany(amc)
                .schemeCategory(schemeCategory)
                .schemeType(schemeType)
                .fundName(record.get("Scheme Name"))
                .fundNavName(record.get("Scheme NAV Name"))
                .code(record.get("Code"))
                .minimumAmount(record.get("Scheme Minimum Amount"))
                .launchDate(getLaunchDate(record.get("Launch Date").trim()))
                .closureDate(getLaunchDate(record.get(" Closure Date").trim()))
                .isinDivPayout(isinParts[0])
                .isinDivReInvestment(isinParts[1])
                .status(Status.INACTIVE)
                .build());
       }
    }

    public List<AssetManagementCompany> getAllAMCs() {
        return amcRepository.findAll();
    }
    
    public AssetManagementCompany createAMC(AssetManagementCompany assetManagementCompany) {
        return amcRepository.save(assetManagementCompany);
    }

    public AssetManagementCompany updateAMC(long id, AssetManagementCompany updatedAMC) {
        Optional<AssetManagementCompany> existingAMC = amcRepository.findById(id);
        if (existingAMC.isPresent()) {
            AssetManagementCompany amc = existingAMC.get();
            amc.setName(updatedAMC.getName());
            amc.setStatus(updatedAMC.getStatus());
            amc.setAmcFunds(updatedAMC.getAmcFunds());
            amc.setAmcDetails(updatedAMC.getAmcDetails());
            return amcRepository.save(amc);
        }
        return null;
    }

    public void deleteAMC(long id) {
        Optional<AssetManagementCompany> existingAMC = amcRepository.findById(id);
        existingAMC.ifPresent(amcRepository::delete);
    }
}