package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> SchemeTypeRepository
 *      - InitialVersion
 */

@Repository
public interface SchemeTypeRepository extends JpaRepository<SchemeType, Long> {
    Optional<SchemeType> findByType(String type);
}
