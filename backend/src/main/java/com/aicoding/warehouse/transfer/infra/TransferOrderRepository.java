package com.aicoding.warehouse.transfer.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferOrderRepository extends JpaRepository<TransferOrderEntity, Long> {

    Optional<TransferOrderEntity> findByOrderNo(String orderNo);

    @Query("SELECT o FROM TransferOrderEntity o WHERE (:status IS NULL OR o.status = :status) AND o.deleted = 0 ORDER BY o.createdAt DESC")
    Page<TransferOrderEntity> findWithFilters(@Param("status") String status, Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.orderNo, 12, 6) AS int)), 0) FROM TransferOrderEntity o WHERE o.orderNo LIKE :prefix")
    int findMaxSeq(@Param("prefix") String prefix);
}