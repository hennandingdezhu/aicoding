package com.aicoding.warehouse.log.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Long> {

    @Query("SELECT o FROM OperationLogEntity o WHERE (:moduleName IS NULL OR o.moduleName = :moduleName) AND (:operationType IS NULL OR o.operationType = :operationType) ORDER BY o.createdAt DESC")
    Page<OperationLogEntity> findWithFilters(@Param("moduleName") String moduleName, @Param("operationType") String operationType, Pageable pageable);
}