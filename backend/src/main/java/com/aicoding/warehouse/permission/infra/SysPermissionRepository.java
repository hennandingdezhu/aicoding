package com.aicoding.warehouse.permission.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
    List<SysPermission> findAllByOrderBySortOrderAsc();
    Optional<SysPermission> findById(Long id);
    List<SysPermission> findAllById(Iterable<Long> ids);
}
