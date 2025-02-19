package com.digibyte.midfin_wealth.mutualFund.repository;

import com.digibyte.midfin_wealth.mutualFund.entity.SchemeSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -19-02-2025 <NaveenDhanasekaran> SubCategoryRepository
 *      - InitialVersion
 */

@Repository
public interface SubCategoryRepository extends JpaRepository<SchemeSubCategory , Long> {
    Optional<SchemeSubCategory> findBySubCategory(String subCategory);
}
