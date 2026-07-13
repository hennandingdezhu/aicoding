package com.aicoding.warehouse.permission.domain;

import java.util.List;

public interface PermissionService {
    List<PermissionNode> getPermissionTree();

    record PermissionNode(Long id, Long parentId, String permissionCode, String permissionName,
                          String permissionType, String path, String component, Integer sortOrder,
                          Integer status, List<PermissionNode> children) {}
}
