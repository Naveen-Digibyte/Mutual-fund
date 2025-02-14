package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchemeTypeRepository extends JpaRepository<SchemeType, Long> {
    Optional<SchemeType> findByType(String type);
}
