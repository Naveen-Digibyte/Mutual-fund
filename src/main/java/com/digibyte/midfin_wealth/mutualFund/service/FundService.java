package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCFundsRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.SchemeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FundService {
    
    @Autowired
    private AMCFundsRepository amcFundsRepository;
    @Autowired
    private SchemeTypeRepository schemeTypeRepository;
    
    public String createFund(FundRequestModel requestModel){

        Optional<AMCFund> fundExist = amcFundsRepository.findByCode(requestModel.getCode());
        if(fundExist.isEmpty()) {
            AMCFund amcFund = AMCFund.builder()
                    .code(requestModel.getCode())
                    .fundName(requestModel.getFundName())
                    .fundNavName(requestModel.getNavName())
                    .schemeType(requestModel.getSchemeType())
                    .schemeCategory(requestModel.getSchemeCategory())
                    .minimumAmount(String.valueOf(requestModel.getMinimumAmount()))
                    .status(Status.ACTIVE)
                    .isinDivPayout(requestModel.getIsin())
                    .launchDate(requestModel.getLaunchDate())
                    .closureDate(requestModel.getClosureDate())
                    .assetManagementCompany(requestModel.getAmc())
                    .build();
            
            amcFundsRepository.save(amcFund);
            return "Fund created successfully.";
        }
        return "Fund Code already exist.";
        
    }
}
