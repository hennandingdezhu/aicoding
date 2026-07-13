package com.aicoding.warehouse.warehouse.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    @Query("SELECT w FROM Warehouse w WHERE w.warehouseCode = :code AND w.deleted = 0")
    Optional<Warehouse> findByCode(@Param("code") String code);

    @Query("SELECT w FROM Warehouse w WHERE w.id = :id AND w.deleted = 0")
    Optional<Warehouse> findByIdActive(@Param("id") Long id);
}
