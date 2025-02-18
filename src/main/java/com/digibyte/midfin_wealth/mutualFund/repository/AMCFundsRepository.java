package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AMCFundsRepository extends JpaRepository<AMCFund, Long> {

    Optional<AMCFund> findByCode(String code);
    @Query("SELECT f FROM AMCFund f JOIN FETCH f.assetManagementCompany WHERE f.assetManagementCompany.id = :amcId")
    List<AMCFund> findFundsByAssetManagementCompanyId(@Param("amcId") long amcId);
    @Query("SELECT a FROM AMCFund a WHERE a.closureDate IS NULL")
    List<AMCFund> findByClosureDateNull();
    @Query("SELECT a FROM AMCFund a WHERE a.closureDate IS NULL")
    Page<AMCFund> findByClosureDateNull(Pageable pageable);

    @Query(value = "SELECT a.* FROM mf_003_t_amc_fund a " +
            "LEFT JOIN LATERAL (SELECT nav.* FROM mf_005_t_nav nav " +
            "WHERE nav.mf_005_amc_fund = a.mf_003_fund_id " +
            "ORDER BY nav.mf_005_date DESC LIMIT 1) AS nav ON true " +
            "WHERE a.mf_003_fund_closure_date IS NULL OR a.mf_003_fund_closure_date = ''", nativeQuery = true)
    List<AMCFund> findFundsWithLatestNavData();

    AMCFund findByAssetManagementCompanyAndCode(AssetManagementCompany assetManagementCompany, String code);
    List<AMCFund> findByAssetManagementCompanyAndSchemeCategory(AssetManagementCompany assetManagementCompany, SchemeCategory schemeCategory);
    List<AMCFund> findByAssetManagementCompanyAndSchemeType(AssetManagementCompany assetManagementCompany, SchemeType schemeType);
    List<AMCFund> findByAssetManagementCompanyAndFundName(AssetManagementCompany assetManagementCompany, String fundName);
    List<AMCFund> findByAssetManagementCompanyAndLaunchDate(AssetManagementCompany assetManagementCompany, LocalDate launchDate);
    List<AMCFund> findByAssetManagementCompanyAndIsinDivPayout(AssetManagementCompany assetManagementCompany, String isinDivPayout);
}
