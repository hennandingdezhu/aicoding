package com.aicoding.warehouse.role.domain;

import com.aicoding.warehouse.common.PageResult;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RoleService {
    PageResult<RoleInfo> listRoles(String keyword, Pageable pageable);
    RoleInfo createRole(CreateRoleCommand command);
    RoleInfo updateRole(Long id, UpdateRoleCommand command);
    void deleteRole(Long id);
    void assignPermissions(Long roleId, AssignPermissionsCommand command);
    List<Long> getRolePermissionIds(Long roleId);

    record RoleInfo(Long id, String roleCode, String roleName, String description, Integer status,
                    java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {}
    record CreateRoleCommand(String roleCode, String roleName, String description) {}
    record UpdateRoleCommand(String roleName, String description) {}
    record AssignPermissionsCommand(java.util.List<Long> permissionIds) {}
}
