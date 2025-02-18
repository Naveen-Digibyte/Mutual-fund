package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.AssetManagementCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AMCRepository extends JpaRepository<AssetManagementCompany, Long> {
    Optional<AssetManagementCompany> findByName(String name);
    
    @Query("SELECT a.assetManagementCompany FROM AMCFund a WHERE a.id = :id")
    Optional<AssetManagementCompany> findAssetManagementCompanyByAmcFundId(@Param("id") long fundId);
}
