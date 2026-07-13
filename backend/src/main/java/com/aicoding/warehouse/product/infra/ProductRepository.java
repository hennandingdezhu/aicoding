package com.aicoding.warehouse.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.productCode = :code AND p.deleted = 0")
    Optional<Product> findByCode(@Param("code") String code);
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deleted = 0")
    Optional<Product> findByIdActive(@Param("id") Long id);
}
