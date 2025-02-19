package com.digibyte.midfin_wealth.mutualFund.model;

import com.digibyte.midfin_wealth.mutualFund.entity.NavData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> FundResponseModel
 *      - InitialVersion
 */

@Data
@Builder
public class FundResponseModel {
    private String amcName;
    private String schemeName;
    private String schemeNavName;
    private String isin;
    private LocalDate launchDate;
    private LocalDate closeDate;
    private NavData navData;
}
