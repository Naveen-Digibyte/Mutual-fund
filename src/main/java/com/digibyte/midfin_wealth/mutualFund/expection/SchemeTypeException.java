package com.digibyte.midfin_wealth.mutualFund.expection;

public class SchemeTypeException extends RuntimeException {

    public SchemeTypeException(String message) {
        super(message);
    }

    public SchemeTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}