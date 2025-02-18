package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.LoggerConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.NavData;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCFundsRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.NavRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
            throw new Exception("Failed to make API request", e);
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
            throw new RuntimeException("Data cleaning failed", e);
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
                    if(fundExist.isEmpty()){
                        throw new EntityNotFoundException("AMC Fund not found: " + record.get(0));
                    }
                    Optional<AssetManagementCompany>assetManagementCompany = amcRepository.findAssetManagementCompanyByAmcFundId(fundExist.get().getId());
                    assetManagementCompany.get().setStatus(Status.ACTIVE);
                    amcRepository.save(assetManagementCompany.get());
                    fundExist.get().setStatus(Status.ACTIVE);
                    amcFundsRepository.save(fundExist.get());
                    NavData navData = NavData.builder()
                            .amcFund(fundExist.get())
                            .value(parseValue(record.get(4)))
                            .date(record.get(7))
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
            throw new RuntimeException("Invalid value format: " + value, e);
        }
    }

    public void readExcelFile(InputStream inputStream) {
        try (Workbook workbook = new HSSFWorkbook(inputStream)) {  
            Sheet sheet = workbook.getSheetAt(0); 

            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
                            break;
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
