package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.AMCFund;
import com.digibyte.midfin_wealth.mutualFund.entity.NavData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> NavRepository
 *      - InitialVersion
 */

@Repository
public interface NavRepository extends JpaRepository<NavData, Long> {
    List<NavData> findByAmcFund_Id(Long amcFundId);
    Optional<NavData> findTopByAmcFund_IdOrderByDateDesc(Long amcFundId);
    boolean existsByAmcFundAndDate(AMCFund amcFund, LocalDate date);
}
