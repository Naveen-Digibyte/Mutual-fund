package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCFundsRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.SchemeCategoryRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.SchemeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AMCService {

    private final AMCRepository amcRepository;
    private final RestTemplate restTemplate;
    private final SchemeCategoryRepository schemeRepository;
    private final AMCFundsRepository amcFundsRepository;
    private final SchemeTypeRepository schemeTypeRepository;

    @Value("${amfiAPI.schemeData}")
    private String SchemeDataUrl;


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
            throw new FundException(ErrorConstants.E_003);
        }
    }

    private void processRecord(CSVRecord record) {
        AssetManagementCompany amc = getOrCreateAMC(record.get("AMC"));
        SchemeCategory schemeCategory = getOrCreateSchemeCategory(record.get("Scheme Category"));
        SchemeType schemeType = getOrCreateSchemeType(record.get("Scheme Type"));
        String isin = record.get("ISIN Div Payout/ ISIN GrowthISIN Div Reinvestment");
        String[] isinParts = parseIsin(isin);
        createOrUpdateFund(record, amc, schemeCategory, schemeType, isinParts);
    }

    private AssetManagementCompany getOrCreateAMC(String amcName) {
        return amcRepository.findByName(amcName).orElseGet(() -> amcRepository.save(AssetManagementCompany.builder().name(amcName).build()));
    }

    private SchemeCategory getOrCreateSchemeCategory(String category) {
        return schemeRepository.findByCategory(category).orElseGet(() -> schemeRepository.save(SchemeCategory.builder().category(category).build()));
    }

    private SchemeType getOrCreateSchemeType(String type) {
        return schemeTypeRepository.findByType(type).orElseGet(() -> schemeTypeRepository.save(SchemeType.builder().type(type).build()));
    }

    private String[] parseIsin(String isin) {
        if (isin == null || isin.isBlank()) {
            return new String[]{null, null, null};
        }
        String[] val = isin.split("IN");
        String payout = val.length > 1 ? "IN" + val[1] : null;
        String growth = val.length > 2 ? "IN" + val[2] : null;
        String reinvestment = val.length > 3 ? "IN" + val[3] : null;
        return new String[]{payout, growth, reinvestment};
    }

    private void createOrUpdateFund(CSVRecord record, AssetManagementCompany amc, SchemeCategory schemeCategory, SchemeType schemeType, String[] isinParts) {
        Optional<AMCFund> fundExists = amcFundsRepository.findByCode(record.get("Code"));
        fundExists.orElseGet(() -> amcFundsRepository.save(AMCFund.builder().assetManagementCompany(amc).schemeCategory(schemeCategory).schemeType(schemeType).fundName(record.get("Scheme Name")).fundNavName(record.get("Scheme NAV Name")).code(record.get("Code")).minimumAmount(record.get("Scheme Minimum Amount")).launchDate(record.get("Launch Date")).closureDate(record.get("Closure Date")).isinGrowth(isinParts[1]).isinDivPayout(isinParts[0]).isinDivReInvestment(isinParts[2]).build()));
    }

    public List<AssetManagementCompany> getAllAMCs(){
        return amcRepository.findAll();
    }

    public List<SchemeCategory> getALlSchemeCategories(){
        return schemeRepository.findAll();
    }

    public List<SchemeType> getAllSchemeTypes(){
        return schemeTypeRepository.findAll();
    }

    public List<AMCFund> getFundsByAssetManagementCompanyId(long amcId) {
        List<AMCFund> funds = amcFundsRepository.findFundsByAssetManagementCompanyId(amcId);
        if (funds.isEmpty()) {
            boolean amcExists = amcRepository.existsById(amcId);
            if (!amcExists) {
                throw new IllegalArgumentException(String.format(ErrorConstants.E_004));
            }
            throw new IllegalStateException(String.format(ErrorConstants.E_005, amcId));
        }
        return funds;
    }

    public AMCFund getFundByAssetManagementCompanyIdAndCode(long amcId, String code) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        AMCFund fund = amcFundsRepository.findByAssetManagementCompanyAndCode(assetManagementCompany, code);

        if (fund == null) {
            throw new IllegalStateException(String.format(ErrorConstants.E_006, code, amcId));
        }
        return fund;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndSchemeCategory(long amcId, long schemeCategoryId) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        SchemeCategory schemeCategory = schemeRepository.findById(schemeCategoryId)
                .orElseThrow(()-> new IllegalArgumentException(String.format(ErrorConstants.E_013)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndSchemeCategory(assetManagementCompany, schemeCategory);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_007, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndSchemeType(long amcId, long schemeTypeId) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        SchemeType schemeType = schemeTypeRepository.findById(schemeTypeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_014)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndSchemeType(assetManagementCompany, schemeType);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_008, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndFundName(long amcId, String fundName) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndFundName(assetManagementCompany, fundName);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_009, fundName, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndLaunchDate(long amcId, String launchDate) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndLaunchDate(assetManagementCompany, launchDate);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_010, launchDate, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndIsinDivPayout(long amcId, String isinDivPayout) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndIsinDivPayout(assetManagementCompany, isinDivPayout);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_011, isinDivPayout, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndIsinGrowth(long amcId, String isinGrowth) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndIsinGrowth(assetManagementCompany, isinGrowth);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_012, isinGrowth, amcId));
        }
        return funds;
    }
}