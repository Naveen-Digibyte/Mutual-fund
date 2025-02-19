package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.model.FundResponseModel;
import com.digibyte.midfin_wealth.mutualFund.repository.AMCFundsRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.NavRepository;
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
    
    @Autowired
    private NavRepository navRepository;
    
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
            return Constants.FUND_CREATED;
        }
        throw new FundException(String.format(ErrorConstants.E_018,requestModel.getFundName()));
        
    }
    
    public FundResponseModel getfundById(long fundId){
        Optional<AMCFund> fundExist = amcFundsRepository.findById(fundId);
        if (fundExist.isPresent()){
            return FundResponseModel.builder()
                    .amcName(fundExist.get().getAssetManagementCompany().getName())
                    .schemeName(fundExist.get().getFundName())
                    .schemeNavName(fundExist.get().getFundNavName())
                    .isin(fundExist.get().getIsinDivPayout())
                    .launchDate(fundExist.get().getLaunchDate())
                    .closeDate(fundExist.get().getClosureDate())
                    .navData(navRepository.findTopByAmcFund_IdOrderByDateDesc(fundId).get())
                    .build();
        }
        throw new FundException(ErrorConstants.E_015);
    }
}
