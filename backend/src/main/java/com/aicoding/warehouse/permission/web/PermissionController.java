package com.aicoding.warehouse.permission.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.permission.domain.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/tree")
    public ApiResponse<List<PermissionService.PermissionNode>> getPermissionTree() {
        return ApiResponse.ok(permissionService.getPermissionTree());
    }
}
