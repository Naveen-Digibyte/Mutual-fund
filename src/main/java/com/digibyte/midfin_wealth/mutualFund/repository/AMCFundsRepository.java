package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AMCFundsRepository extends JpaRepository<AMCFund, Long> {

    Optional<AMCFund> findByCode(String code);
    @Query("SELECT f FROM AMCFund f JOIN FETCH f.assetManagementCompany WHERE f.assetManagementCompany.id = :amcId")
    List<AMCFund> findFundsByAssetManagementCompanyId(@Param("amcId") long amcId);
    AMCFund findByAssetManagementCompanyAndCode(AssetManagementCompany assetManagementCompany, String code);
    List<AMCFund> findByAssetManagementCompanyAndSchemeCategory(AssetManagementCompany assetManagementCompany, SchemeCategory schemeCategory);
    List<AMCFund> findByAssetManagementCompanyAndSchemeType(AssetManagementCompany assetManagementCompany, SchemeType schemeType);
    List<AMCFund> findByAssetManagementCompanyAndFundName(AssetManagementCompany assetManagementCompany, String fundName);
    List<AMCFund> findByAssetManagementCompanyAndLaunchDate(AssetManagementCompany assetManagementCompany, String launchDate);
    List<AMCFund> findByAssetManagementCompanyAndIsinDivPayout(AssetManagementCompany assetManagementCompany, String isinDivPayout);
    List<AMCFund> findByAssetManagementCompanyAndIsinGrowth(AssetManagementCompany assetManagementCompany, String isinGrowth);

}
