package com.aicoding.warehouse.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    List<SysUserRole> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByUserIdAndRoleIdIn(Long userId, List<Long> roleIds);
}
