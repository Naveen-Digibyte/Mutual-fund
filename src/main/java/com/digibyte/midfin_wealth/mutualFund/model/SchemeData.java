package com.digibyte.midfin_wealth.mutualFund.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> SchemeData
 *      - InitialVersion
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemeData {
    private String amc;
    private String code;
    private String schemeName;
    private String schemeType;
    private String schemeCategory;
    private String schemeNavName;
    private String schemeMinimumAmount;
    private String launchDate;
    private String closureDate;
    private String isinDivPayout;
    private String isinGrowth;
    private String isinDivReinvestment;
}
