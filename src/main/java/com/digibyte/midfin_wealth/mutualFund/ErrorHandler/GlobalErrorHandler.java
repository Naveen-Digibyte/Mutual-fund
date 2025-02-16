package com.digibyte.midfin_wealth.mutualFund.ErrorHandler;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(FundException.class)
    public ResponseEntity<Object> handleUserException(final FundException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e));
    }

    private ResponseModel createResponse(Exception e) {
        return new ResponseModel(Constants.NEGATIVE, e.getMessage(), null);
    }
}
