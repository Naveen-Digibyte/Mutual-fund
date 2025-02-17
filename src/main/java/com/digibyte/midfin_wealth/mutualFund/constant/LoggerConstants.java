package com.digibyte.midfin_wealth.mutualFund.constant;

public class LoggerConstants {

    // Info messages
    public static final String FETCH_DATA_START = "Starting to fetch data from API.";
    public static final String DATA_FETCH_SUCCESS = "Data fetched successfully from the API.";
    public static final String DATA_PARSED_SAVED_SUCCESS = "Data parsed and saved successfully.";
    public static final String PARSE_SAVE_DATA_START = "Starting to parse and save data.";
    public static final String DATA_CLEANED_SUCCESS = "Data cleaned successfully.";

    // Debug messages
    public static final String API_URL_GENERATED = "Generated API URL: {}";
    public static final String CLEANED_DATA = "Cleaned data: \n{}";
    public static final String PARSED_RECORD = "Parsed record: {}";

    // Error messages
    public static final String FETCH_DATA_ERROR = "Error occurred while fetching or processing data from API: {}";
    public static final String DATA_PARSING_ERROR = "Error while parsing CSV data: {}";
    public static final String RECORD_PARSING_ERROR = "Error parsing record: {}";
    public static final String HTTP_ERROR_FETCH = "HTTP error occurred while fetching data from the API: {}";
    public static final String API_REQUEST_ERROR = "Error occurred while making the API request: {}";
    public static final String INVALID_NUMBER_FORMAT = "Invalid number format for NAV data: {}";
    public static final String AMC_FUND_NOT_FOUND = "AMC Fund not found for record {}: {}";
    public static final String DATA_CLEANING_ERROR = "Error cleaning data: {}";
}
