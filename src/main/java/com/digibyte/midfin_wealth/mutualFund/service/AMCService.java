package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import com.digibyte.midfin_wealth.mutualFund.model.SchemeData;
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
import java.util.ArrayList;
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


    public List<SchemeData> getAmcDetails() {
        ResponseEntity<String> response = restTemplate.getForEntity(SchemeDataUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String csvContent = response.getBody();
            return parseCsv(csvContent);
        } else {
            throw new RuntimeException("Failed to fetch data from API");
        }
    }

    private List<SchemeData> parseCsv(String csvContent) {
        List<SchemeData> SchemeDatas = new ArrayList<>();
        try {
            StringReader reader = new StringReader(csvContent);
            CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord record : records) {
                Optional<AssetManagementCompany> amcExits = amcRepository.findByName(record.get("AMC"));
                Optional<SchemeCategory> schemeExists = schemeRepository.findByCategory(record.get("Scheme Category"));
                Optional<SchemeType> schemeTypeExist = schemeTypeRepository.findByType(record.get("Scheme Type"));
                String isin = record.get("ISIN Div Payout/ ISIN GrowthISIN Div Reinvestment");
                String[] val;
                String growth;
                String payout;
                String reinvestment = null;
                if (!isin.isBlank() || !isin.isEmpty()) {
                    if (isin.startsWith("IN")) {
                        val = isin.split("IN");
                        payout = "IN" + val[1];
                        if (val.length > 2) {
                            growth = "IN" + val[2];
                            if (val.length > 3) {
                                reinvestment = "IN" + val[3];
                            }
                        } else {
                            growth = null;
                        }
                    } else {
                        payout = null;
                        growth = null;
                    }
                } else {
                    payout = null;
                    growth = null;
                }
                SchemeCategory schemeCategory = schemeExists.orElseGet(() -> schemeRepository.save(SchemeCategory.builder().category(record.get("Scheme Category")).build()));
                AssetManagementCompany assetManagementCompany = amcExits.orElseGet(() -> amcRepository.save(AssetManagementCompany.builder().name(record.get("AMC")).build()));
                SchemeType schemeType = schemeTypeExist.orElseGet(() -> schemeTypeRepository.save(SchemeType.builder().type(record.get("Scheme Type")).build()));
                amcFundsRepository.save(AMCFund.builder()
                        .assetManagementCompany(assetManagementCompany)
                        .schemeCategory(schemeCategory)
                        .schemeType(schemeType)
                        .fundName(record.get("Scheme Name"))
                        .fundNavName(record.get("Scheme NAV Name"))
                        .code(record.get("Code"))
                        .minimumAmount(record.get("Scheme Minimum Amount"))
                        .launchDate(record.get("Launch Date"))
                        .closureDate(record.get(" Closure Date"))
                        .isinGrowth(growth)
                        .isinDivPayout(payout)
                        .isinDivReInvestment(reinvestment)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV data", e);
        }
        return SchemeDatas;
    }
}