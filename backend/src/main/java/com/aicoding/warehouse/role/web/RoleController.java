package com.aicoding.warehouse.role.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.role.domain.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ApiResponse<PageResult<RoleService.RoleInfo>> listRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "") String keyword) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return ApiResponse.ok(roleService.listRoles(keyword, pageable));
    }

    @PostMapping
    public ApiResponse<RoleService.RoleInfo> createRole(@Valid @RequestBody CreateRoleRequest request) {
        var cmd = new RoleService.CreateRoleCommand(request.roleCode(), request.roleName(), request.description());
        return ApiResponse.ok(roleService.createRole(cmd));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleService.RoleInfo> updateRole(@PathVariable Long id,
                                                         @RequestBody UpdateRoleRequest request) {
        var cmd = new RoleService.UpdateRoleCommand(request.roleName(), request.description());
        return ApiResponse.ok(roleService.updateRole(id, cmd));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.ok();
    }

    @PutMapping("/{id}/permissions")
    public ApiResponse<Void> assignPermissions(@PathVariable Long id,
                                               @RequestBody AssignPermissionsRequest request) {
        var cmd = new RoleService.AssignPermissionsCommand(request.permissionIds());
        roleService.assignPermissions(id, cmd);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}/permissions")
    public ApiResponse<List<Long>> getRolePermissionIds(@PathVariable Long id) {
        return ApiResponse.ok(roleService.getRolePermissionIds(id));
    }
}
