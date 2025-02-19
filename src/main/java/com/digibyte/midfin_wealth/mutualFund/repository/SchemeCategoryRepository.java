package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.SchemeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchemeCategoryRepository extends JpaRepository<SchemeCategory, Long> {
    Optional<SchemeCategory> findByCategory(String category);
}
