package com.aicoding.warehouse.productcategory.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    @Query("SELECT c FROM ProductCategory c WHERE c.categoryCode = :code AND c.deleted = 0")
    Optional<ProductCategory> findByCode(@Param("code") String code);

    @Query("SELECT c FROM ProductCategory c WHERE c.id = :id AND c.deleted = 0")
    Optional<ProductCategory> findByIdActive(@Param("id") Long id);

    @Query("SELECT c FROM ProductCategory c WHERE c.deleted = 0 ORDER BY c.sortOrder ASC")
    List<ProductCategory> findAllActive();
}
