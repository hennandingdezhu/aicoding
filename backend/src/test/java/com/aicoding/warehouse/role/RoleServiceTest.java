package com.aicoding.warehouse.role;

import com.aicoding.warehouse.role.domain.RoleService;
import com.aicoding.warehouse.role.infra.SysRole;
import com.aicoding.warehouse.role.infra.SysRoleRepository;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @BeforeEach
    void setUp() {
        SysRole role = new SysRole();
        role.setRoleCode("admin");
        role.setRoleName("管理员");
        role.setDescription("系统管理员");
        role.setStatus(1);
        sysRoleRepository.save(role);
    }

    @Test
    void listRolesShouldReturnPaginatedResults() {
        PageResult<RoleService.RoleInfo> result = roleService.listRoles("admin", PageRequest.of(0, 20));
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
        assertThat(result.getRecords().get(0).roleCode()).isEqualTo("admin");
    }

    @Test
    void listRolesShouldReturnEmptyWhenKeywordNotMatch() {
        PageResult<RoleService.RoleInfo> result = roleService.listRoles("nonexistent", PageRequest.of(0, 20));
        assertThat(result.getRecords()).isEmpty();
    }

    @Test
    void createRoleShouldSucceed() {
        var cmd = new RoleService.CreateRoleCommand("warehouse_manager", "仓库主管", "管理仓库");
        RoleService.RoleInfo result = roleService.createRole(cmd);

        assertThat(result.id()).isNotNull();
        assertThat(result.roleCode()).isEqualTo("warehouse_manager");
        assertThat(result.roleName()).isEqualTo("仓库主管");
        assertThat(result.status()).isEqualTo(1);
    }

    @Test
    void createRoleShouldFailWhenRoleCodeExists() {
        var cmd = new RoleService.CreateRoleCommand("admin", "重复管理员", "重复");
        assertThatThrownBy(() -> roleService.createRole(cmd))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("角色编码已存在");
    }

    @Test
    void updateRoleShouldSucceed() {
        SysRole saved = sysRoleRepository.findAll().get(0);
        var cmd = new RoleService.UpdateRoleCommand("超级管理员", "更新描述");
        RoleService.RoleInfo result = roleService.updateRole(saved.getId(), cmd);

        assertThat(result.roleName()).isEqualTo("超级管理员");
        assertThat(result.description()).isEqualTo("更新描述");
    }

    @Test
    void updateRoleShouldFailWhenNotFound() {
        var cmd = new RoleService.UpdateRoleCommand("不存在", "不存在");
        assertThatThrownBy(() -> roleService.updateRole(9999L, cmd))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("角色不存在");
    }

    @Test
    void deleteRoleShouldSoftDelete() {
        SysRole saved = sysRoleRepository.findAll().get(0);
        roleService.deleteRole(saved.getId());

        var found = sysRoleRepository.findByIdAndDeleted(saved.getId(), 0);
        assertThat(found).isEmpty();
    }

    @Test
    void assignPermissionsShouldSucceed() {
        SysRole saved = sysRoleRepository.findAll().get(0);
        var cmd = new RoleService.AssignPermissionsCommand(java.util.List.of(1L, 2L, 3L));
        roleService.assignPermissions(saved.getId(), cmd);

        var permIds = roleService.getRolePermissionIds(saved.getId());
        assertThat(permIds).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void assignPermissionsShouldReplaceExisting() {
        SysRole saved = sysRoleRepository.findAll().get(0);
        roleService.assignPermissions(saved.getId(), new RoleService.AssignPermissionsCommand(java.util.List.of(1L)));
        roleService.assignPermissions(saved.getId(), new RoleService.AssignPermissionsCommand(java.util.List.of(4L, 5L)));

        var permIds = roleService.getRolePermissionIds(saved.getId());
        assertThat(permIds).containsExactlyInAnyOrder(4L, 5L);
    }
}
