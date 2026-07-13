package com.aicoding.warehouse.warehouselocation.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long>, JpaSpecificationExecutor<WarehouseLocation> {
    @Query("SELECT l FROM WarehouseLocation l WHERE l.locationCode = :code AND l.deleted = 0")
    Optional<WarehouseLocation> findByCode(@Param("code") String code);
    @Query("SELECT l FROM WarehouseLocation l WHERE l.id = :id AND l.deleted = 0")
    Optional<WarehouseLocation> findByIdActive(@Param("id") Long id);
}
