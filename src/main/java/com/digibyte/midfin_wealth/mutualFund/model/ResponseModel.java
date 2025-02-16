package com.digibyte.midfin_wealth.mutualFund.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseModel {

    private String status;

    private String message;

    private Object data;

}