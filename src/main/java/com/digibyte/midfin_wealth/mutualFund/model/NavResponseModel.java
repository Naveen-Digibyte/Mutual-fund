package com.digibyte.midfin_wealth.mutualFund.model;

import com.digibyte.midfin_wealth.mutualFund.entity.NavData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> NavResponseModel
 *      - InitialVersion
 */

@Data
@Builder
public class NavResponseModel {
    private String amcName;
    private String SchemeNavName;
    private String SchemeName;
    private String schemeCode;
    private List<NavData> data;
}
