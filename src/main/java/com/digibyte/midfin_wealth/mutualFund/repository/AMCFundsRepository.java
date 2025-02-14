package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AMCFundsRepository extends JpaRepository<AMCFund, Long> {
}
