package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.constant.LoggerConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.NavData;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.model.NavResponseModel;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCFundsRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.NavRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NavService {
    private static final Logger logger = LoggerFactory.getLogger(NavService.class);
    private final NavRepository navRepository;
    private final RestTemplate restTemplate;
    private final AMCFundsRepository amcFundsRepository;
    private final AMCRepository amcRepository;

    @Value("${amfiAPI.dailyNavData}")
    private String dailyNavUrl;
    
    @Value("${amfi.navHistoryData}")
    private String API_URL;
    
    public void fetchAndSaveDataFromApi() throws Exception {
        logger.info(LoggerConstants.FETCH_DATA_START);

        String apiUrl = dailyNavUrl + LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
        logger.debug(LoggerConstants.API_URL_GENERATED, apiUrl);

        try {
            String data = restTemplate.getForObject(apiUrl, String.class);
            logger.info(LoggerConstants.DATA_FETCH_SUCCESS);

            String cleanedData = cleanData(data);
            logger.debug(LoggerConstants.CLEANED_DATA, cleanedData);

            parseAndSaveData(cleanedData);
            logger.info(LoggerConstants.DATA_PARSED_SAVED_SUCCESS);
        } catch (RestClientException e) {
            logger.error(LoggerConstants.API_REQUEST_ERROR, e.getMessage(), e);
            throw new Exception(ErrorConstants.E_001);
        } catch (Exception e) {
            logger.error(LoggerConstants.FETCH_DATA_ERROR, e.getMessage(), e);
            throw e;
        }
    }

    private String cleanData(String data) {
        try {
            logger.debug(LoggerConstants.DATA_CLEANED_SUCCESS);
            return data.lines()
                    .filter(line -> line.contains(";") && !line.trim().isEmpty())
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            logger.error(LoggerConstants.DATA_CLEANING_ERROR, e.getMessage(), e);
            throw new RuntimeException(ErrorConstants.E_003);
        }
    }

    public void parseAndSaveData(String data) throws Exception {
        logger.info(LoggerConstants.PARSE_SAVE_DATA_START);

        List<NavData> schemes = new ArrayList<>();
        try (StringReader reader = new StringReader(data)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(';')
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : records) {
                try {
                    Optional<AMCFund> fundExist = amcFundsRepository.findByCode(record.get(0));
                    if (fundExist.isEmpty()) {
                        throw new EntityNotFoundException(ErrorConstants.E_004);
                    }
                    fundExist.get().setStatus(Status.ACTIVE);
                    fundExist.get().getAssetManagementCompany().setStatus(Status.ACTIVE);
                    amcFundsRepository.save(fundExist.get());
                    LocalDate navDate = LocalDate.parse(record.get(7), DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
                    boolean exists = navRepository.existsByAmcFundAndDate(fundExist.get(), navDate);
                    if (exists) {
                        logger.warn(LoggerConstants.NAV_ALREADY_EXIST, record.get(0), navDate);
                        continue;
                    }
                    NavData navData = NavData.builder()
                            .amcFund(fundExist.get())
                            .value(parseValue(record.get(4)))
                            .date(navDate)
                            .build();
                    schemes.add(navData);
                    logger.debug(LoggerConstants.PARSED_RECORD, navData);

                } catch (NumberFormatException e) {
                    logger.error(LoggerConstants.INVALID_NUMBER_FORMAT, record.get(4), e);
                } catch (EntityNotFoundException e) {
                    logger.error(LoggerConstants.AMC_FUND_NOT_FOUND, record, e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(LoggerConstants.RECORD_PARSING_ERROR, record, e);
                }
            }
            navRepository.saveAll(schemes);
            logger.info(LoggerConstants.DATA_PARSED_SAVED_SUCCESS);
        } catch (Exception e) {
            logger.error(LoggerConstants.DATA_PARSING_ERROR, e.getMessage(), e);
            throw e;
        }
    }


    private float parseValue(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logger.error(LoggerConstants.INVALID_NUMBER_FORMAT, value, e);
            throw new FundException(ErrorConstants.E_016 + value);
        }
    }
    
    public String getSaveNav(String schemeCode, String amcId, LocalDate fromDate, LocalDate toDate) {
        Optional<AMCFund> fundExist = amcFundsRepository.findByCode(schemeCode);

        if (fundExist.isPresent()) {
            AMCFund fund = fundExist.get();
            fund.setStatus(Status.ACTIVE);
            fund.getAssetManagementCompany().setStatus(Status.ACTIVE);
            amcFundsRepository.save(fund);

            String html = fetchNavHistory(amcId, schemeCode, fromDate, toDate);

            List<NavData> navList = extractNavData(html, fund);
            navRepository.saveAll(navList);
            return String.format(Constants.DATA_PROCESSED_SUCCESSFUL, schemeCode);
        }
        throw new FundException(String.format(ErrorConstants.E_017, schemeCode));
    }

    private String fetchNavHistory(String amcId, String schemeCode, LocalDate fromDate, LocalDate toDate) {
        RestTemplate restTemplate = new RestTemplate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedFromDate = fromDate.format(formatter);
        String formattedToDate = toDate.format(formatter);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("mfID", amcId);
        formParams.add("scID", schemeCode);
        formParams.add("fDate", formattedFromDate);
        formParams.add("tDate", formattedToDate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private List<NavData> extractNavData(String html, AMCFund fund) {
        List<NavData> navList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element table = doc.select("#divExcelPeriod table").first();
        if (table != null) {
            Elements rows = table.select("tr");
            for (int i = 5; i < rows.size(); i++) {
                Elements cols = rows.get(i).select("td");
                if (cols.size() == 4) {
                    String navValue = cols.get(0).text();
                    String navDate = cols.get(3).text();
                    LocalDate parsedDate = getLaunchDate(navDate);
                    boolean exists = navRepository.existsByAmcFundAndDate(fund, parsedDate);
                    if (exists) {
                        logger.warn("NAV entry already exists for Fund: {}, Date: {}", fund.getCode(), parsedDate);
                        continue;
                    }
                    navList.add(NavData.builder().amcFund(fund).date(parsedDate).value(Float.parseFloat(navValue)).build());
                }
            }
        }
        return navList;
    }

    private LocalDate getLaunchDate(String date){
        if(date.isEmpty()){
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(date.length() == 9 ? "dd-MMM-yy" : "dd-MMM-yyyy", Locale.ENGLISH));
    }
    
    public NavResponseModel getNavByFundCode(long fundId){
        Optional<AMCFund> fundExist = amcFundsRepository.findById(fundId);
        if(fundExist.isPresent()){
            return NavResponseModel.builder()
                    .amcName(fundExist.get().getAssetManagementCompany().getName())
                    .SchemeNavName(fundExist.get().getFundNavName())
                    .schemeCode(fundExist.get().getCode())
                    .SchemeName(fundExist.get().getFundName())
                    .data(navRepository.findByAmcFund_Id(fundId))
                    .build();
        }
        throw new FundException(ErrorConstants.E_015);
    }
}
