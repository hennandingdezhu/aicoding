package com.aicoding.warehouse.inventory.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheckEntity, Long> {

    Optional<InventoryCheckEntity> findByOrderNo(String orderNo);

    @Query("SELECT o FROM InventoryCheckEntity o WHERE (:warehouseId IS NULL OR o.warehouseId = :warehouseId) AND (:status IS NULL OR o.status = :status) AND o.deleted = 0 ORDER BY o.createdAt DESC")
    Page<InventoryCheckEntity> findWithFilters(@Param("warehouseId") Long warehouseId, @Param("status") String status, Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.orderNo, 12, 6) AS int)), 0) FROM InventoryCheckEntity o WHERE o.orderNo LIKE :prefix")
    int findMaxSeq(@Param("prefix") String prefix);
}