package com.aicoding.warehouse.permission;

import com.aicoding.warehouse.permission.domain.PermissionService;
import com.aicoding.warehouse.permission.infra.SysPermission;
import com.aicoding.warehouse.permission.infra.SysPermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PermissionServiceTest {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @BeforeEach
    void setUp() {
        SysPermission p1 = new SysPermission();
        p1.setParentId(null);
        p1.setPermissionCode("user:manage");
        p1.setPermissionName("用户管理");
        p1.setPermissionType("MENU");
        p1.setPath("/user");
        p1.setComponent("UserLayout");
        p1.setSortOrder(1);
        p1.setStatus(1);
        sysPermissionRepository.save(p1);

        SysPermission p2 = new SysPermission();
        p2.setParentId(p1.getId());
        p2.setPermissionCode("user:list");
        p2.setPermissionName("用户列表");
        p2.setPermissionType("BUTTON");
        p2.setSortOrder(1);
        p2.setStatus(1);
        sysPermissionRepository.save(p2);
    }

    @Test
    void getPermissionTreeShouldReturnHierarchicalStructure() {
        List<PermissionService.PermissionNode> tree = permissionService.getPermissionTree();

        assertThat(tree).hasSize(1);
        assertThat(tree.get(0).permissionCode()).isEqualTo("user:manage");
        assertThat(tree.get(0).children()).hasSize(1);
        assertThat(tree.get(0).children().get(0).permissionCode()).isEqualTo("user:list");
    }
}
