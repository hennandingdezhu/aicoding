package com.aicoding.warehouse.supplier.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    @Query("SELECT s FROM Supplier s WHERE s.supplierCode = :code AND s.deleted = 0")
    Optional<Supplier> findByCode(@Param("code") String code);
    @Query("SELECT s FROM Supplier s WHERE s.id = :id AND s.deleted = 0")
    Optional<Supplier> findByIdActive(@Param("id") Long id);
}
