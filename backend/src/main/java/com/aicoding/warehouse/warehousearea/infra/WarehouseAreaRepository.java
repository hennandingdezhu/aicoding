package com.aicoding.warehouse.warehousearea.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WarehouseAreaRepository extends JpaRepository<WarehouseArea, Long>, JpaSpecificationExecutor<WarehouseArea> {
    @Query("SELECT a FROM WarehouseArea a WHERE a.warehouseId = :warehouseId AND a.areaCode = :code AND a.deleted = 0")
    Optional<WarehouseArea> findByWarehouseIdAndCode(@Param("warehouseId") Long warehouseId, @Param("code") String code);
    @Query("SELECT a FROM WarehouseArea a WHERE a.id = :id AND a.deleted = 0")
    Optional<WarehouseArea> findByIdActive(@Param("id") Long id);
}
