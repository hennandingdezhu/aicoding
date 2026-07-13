package com.aicoding.warehouse.role.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {
    List<SysRolePermission> findByRoleId(Long roleId);
    void deleteByRoleId(Long roleId);
}
