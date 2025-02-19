package com.digibyte.midfin_wealth.mutualFund.model;

import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeSubCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> FundRequestModel
 *      - InitialVersion
 */

@Data
public class FundRequestModel {
    private String code;
    private String fundName;
    private String navName;
    private int minimumAmount;
    private LocalDate launchDate;
    private LocalDate closureDate;
    private String isin;
    private SchemeType schemeType;
    private SchemeCategory schemeCategory;
    private SchemeSubCategory schemeSubCategory;
    private AssetManagementCompany amc;
}
