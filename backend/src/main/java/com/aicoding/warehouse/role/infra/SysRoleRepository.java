package com.aicoding.warehouse.role.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {
    boolean existsByRoleCodeAndDeleted(String roleCode, Integer deleted);
    Optional<SysRole> findByIdAndDeleted(Long id, Integer deleted);
}
