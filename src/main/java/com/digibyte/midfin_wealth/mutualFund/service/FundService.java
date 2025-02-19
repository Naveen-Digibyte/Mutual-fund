package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.Constants;
import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import com.digibyte.midfin_wealth.mutualFund.enums.Status;
import com.digibyte.midfin_wealth.mutualFund.expection.FundException;
import com.digibyte.midfin_wealth.mutualFund.model.FundRequestModel;
import com.digibyte.midfin_wealth.mutualFund.model.FundResponseModel;
import com.digibyte.midfin_wealth.mutualFund.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> FundService
 *      - InitialVersion
 */

@Service
@RequiredArgsConstructor
public class FundService {
    
    private final AMCFundsRepository amcFundsRepository;
    private final SchemeTypeRepository schemeTypeRepository;
    private final AMCRepository amcRepository;
    private final SchemeCategoryRepository schemeRepository;
    private final SubCategoryRepository subCategoryRepository;
    
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
                    .schemeSubCategory(requestModel.getSchemeSubCategory())
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

    public List<AMCFund> getFundsByAssetManagementCompanyId(long amcId) {
        List<AMCFund> funds = amcFundsRepository.findFundsByAssetManagementCompanyId(amcId);
        if (funds.isEmpty()) {
            boolean amcExists = amcRepository.existsById(amcId);
            if (!amcExists) {
                throw new IllegalArgumentException(String.format(ErrorConstants.E_004));
            }
            throw new IllegalStateException(String.format(ErrorConstants.E_005, amcId));
        }
        return funds;
    }

    public AMCFund getFundByAssetManagementCompanyIdAndCode(long amcId, String code) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        AMCFund fund = amcFundsRepository.findByAssetManagementCompanyAndCode(assetManagementCompany, code);

        if (fund == null) {
            throw new IllegalStateException(String.format(ErrorConstants.E_006, code, amcId));
        }
        return fund;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndSchemeCategory(long amcId, long schemeCategoryId) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        SchemeCategory schemeCategory = schemeRepository.findById(schemeCategoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_013)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndSchemeCategory(assetManagementCompany, schemeCategory);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_007, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndSchemeType(long amcId, long schemeTypeId) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        SchemeType schemeType = schemeTypeRepository.findById(schemeTypeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_014)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndSchemeType(assetManagementCompany, schemeType);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_008, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndFundName(long amcId, String fundName) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndFundName(assetManagementCompany, fundName);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_009, fundName, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndLaunchDate(long amcId, String launchDate) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndLaunchDate(assetManagementCompany, LocalDate.parse(launchDate));

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_010, launchDate, amcId));
        }
        return funds;
    }

    public List<AMCFund> getFundsByAssetManagementCompanyIdAndIsinDivPayout(long amcId, String isinDivPayout) {
        AssetManagementCompany assetManagementCompany = amcRepository.findById(amcId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorConstants.E_004)));
        List<AMCFund> funds = amcFundsRepository.findByAssetManagementCompanyAndIsinDivPayout(assetManagementCompany, isinDivPayout);

        if (funds.isEmpty()) {
            throw new IllegalStateException(String.format(ErrorConstants.E_011, isinDivPayout, amcId));
        }
        return funds;
    }

    public List<AMCFund> getAllFundsWithNoClosureDate() {
        return amcFundsRepository.findByClosureDateNull();
    }

    public Page<AMCFund> getFundsByPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("code").ascending());
        Page<AMCFund> pageResult = amcFundsRepository.findByClosureDateNull(pageable);
        return pageResult;
    }
}
