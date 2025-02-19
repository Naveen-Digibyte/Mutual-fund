package com.digibyte.midfin_wealth.mutualFund.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> ResponseModel
 *      - InitialVersion
 */

@Data
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseModel {

    private String status;

    private String message;

    private Object data;

}