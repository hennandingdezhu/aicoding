package com.aicoding.warehouse.role.web;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank(message = "角色编码不能为空") String roleCode,
        @NotBlank(message = "角色名称不能为空") String roleName,
        String description
) {}
