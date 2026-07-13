package com.aicoding.warehouse.auth.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface SysUserRepository extends JpaRepository<com.aicoding.warehouse.auth.infra.SysUser, Long>, JpaSpecificationExecutor<com.aicoding.warehouse.auth.infra.SysUser> {
    Optional<com.aicoding.warehouse.auth.infra.SysUser> findByUsernameAndDeleted(String username, Integer deleted);
    Optional<com.aicoding.warehouse.auth.infra.SysUser> findByIdAndDeleted(Long id, Integer deleted);
}
