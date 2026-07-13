package com.aicoding.warehouse.outbound.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutboundOrderRepository extends JpaRepository<OutboundOrderEntity, Long> {

    Optional<OutboundOrderEntity> findByOrderNo(String orderNo);

    @Query("SELECT o FROM OutboundOrderEntity o WHERE (:status IS NULL OR o.status = :status) AND (:warehouseId IS NULL OR o.warehouseId = :warehouseId) AND o.deleted = 0 ORDER BY o.createdAt DESC")
    Page<OutboundOrderEntity> findWithFilters(@Param("status") String status, @Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.orderNo, 13, 6) AS int)), 0) FROM OutboundOrderEntity o WHERE o.orderNo LIKE :prefix")
    int findMaxSeq(@Param("prefix") String prefix);
}