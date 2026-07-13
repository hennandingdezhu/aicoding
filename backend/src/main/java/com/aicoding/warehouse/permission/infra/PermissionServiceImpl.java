package com.aicoding.warehouse.permission.infra;

import com.aicoding.warehouse.permission.domain.PermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final SysPermissionRepository sysPermissionRepository;

    public PermissionServiceImpl(SysPermissionRepository sysPermissionRepository) {
        this.sysPermissionRepository = sysPermissionRepository;
    }

    @Override
    public List<PermissionNode> getPermissionTree() {
        List<SysPermission> all = sysPermissionRepository.findAllByOrderBySortOrderAsc();
        Map<Long, List<SysPermission>> childrenMap = all.stream()
                .filter(p -> p.getParentId() != null)
                .collect(Collectors.groupingBy(SysPermission::getParentId));

        return all.stream()
                .filter(p -> p.getParentId() == null)
                .map(p -> buildNode(p, childrenMap))
                .toList();
    }

    private PermissionNode buildNode(SysPermission p, Map<Long, List<SysPermission>> childrenMap) {
        List<PermissionNode> children = childrenMap.getOrDefault(p.getId(), List.of()).stream()
                .map(child -> buildNode(child, childrenMap))
                .toList();
        return new PermissionNode(p.getId(), p.getParentId(), p.getPermissionCode(),
                p.getPermissionName(), p.getPermissionType(), p.getPath(), p.getComponent(),
                p.getSortOrder(), p.getStatus(), children);
    }
}
