package com.aicoding.warehouse.user;

import com.aicoding.warehouse.auth.infra.SysUser;
import com.aicoding.warehouse.auth.infra.SysUserRepository;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.role.infra.SysRole;
import com.aicoding.warehouse.role.infra.SysRoleRepository;
import com.aicoding.warehouse.user.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SysUser testUser;
    private SysRole testRole;

    @BeforeEach
    void setUp() {
        testUser = new SysUser();
        testUser.setUsername("testuser");
        testUser.setPasswordHash(passwordEncoder.encode("123456"));
        testUser.setRealName("测试用户");
        testUser.setPhone("13800000001");
        testUser.setEmail("test@example.com");
        testUser.setStatus(1);
        sysUserRepository.save(testUser);

        testRole = new SysRole();
        testRole.setRoleCode("operator");
        testRole.setRoleName("操作员");
        testRole.setStatus(1);
        sysRoleRepository.save(testRole);
    }

    @Test
    void listUsersShouldReturnPaginatedResults() {
        PageResult<UserService.UserInfo> result = userService.listUsers("测试", 1, PageRequest.of(0, 20));
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
        assertThat(result.getRecords().get(0).username()).isEqualTo("testuser");
    }

    @Test
    void listUsersShouldFilterByStatus() {
        PageResult<UserService.UserInfo> result = userService.listUsers("test", 0, PageRequest.of(0, 20));
        assertThat(result.getRecords()).isEmpty();
    }

    @Test
    void getUserDetailShouldReturnUserWithRolesAndWarehouses() {
        UserService.UserDetail detail = userService.getUserDetail(testUser.getId());
        assertThat(detail.username()).isEqualTo("testuser");
        assertThat(detail.realName()).isEqualTo("测试用户");
        assertThat(detail.roleIds()).isEmpty();
        assertThat(detail.warehouseIds()).isEmpty();
    }

    @Test
    void createUserShouldSucceedWithRolesAndWarehouses() {
        var cmd = new UserService.CreateUserCommand(
                "newuser", "password123", "新用户", "13800000002",
                "new@example.com", List.of(testRole.getId()), List.of(1L, 2L));

        UserService.UserInfo result = userService.createUser(cmd);
        assertThat(result.id()).isNotNull();
        assertThat(result.username()).isEqualTo("newuser");

        UserService.UserDetail detail = userService.getUserDetail(result.id());
        assertThat(detail.roleIds()).containsExactly(testRole.getId());
        assertThat(detail.warehouseIds()).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void createUserShouldFailWhenUsernameExists() {
        var cmd = new UserService.CreateUserCommand(
                "testuser", "password123", "重复用户", "13800000002",
                "dup@example.com", List.of(), List.of());
        assertThatThrownBy(() -> userService.createUser(cmd))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户名已存在");
    }

    @Test
    void updateUserShouldUpdateRolesAndWarehouses() {
        var cmd = new UserService.UpdateUserCommand(
                "更新姓名", "13800000003", "updated@example.com",
                List.of(testRole.getId()), List.of(10L));

        UserService.UserInfo result = userService.updateUser(testUser.getId(), cmd);
        assertThat(result.realName()).isEqualTo("更新姓名");

        UserService.UserDetail detail = userService.getUserDetail(result.id());
        assertThat(detail.roleIds()).containsExactly(testRole.getId());
        assertThat(detail.warehouseIds()).containsExactly(10L);
    }

    @Test
    void updateUserShouldSucceedWithEmptyRoles() {
        var cmd = new UserService.UpdateUserCommand("更新姓名", "13800000003", "updated@example.com", List.of(), List.of());
        UserService.UserInfo result = userService.updateUser(testUser.getId(), cmd);
        assertThat(result.realName()).isEqualTo("更新姓名");
    }

    @Test
    void deleteUserShouldSoftDelete() {
        userService.deleteUser(testUser.getId());
        var found = sysUserRepository.findById(testUser.getId()).orElseThrow();
        assertThat(found.getDeleted()).isEqualTo(1);
    }

    @Test
    void updateUserStatusShouldChangeStatus() {
        userService.updateUserStatus(testUser.getId(), 0);
        var updated = sysUserRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(0);

        userService.updateUserStatus(testUser.getId(), 1);
        updated = sysUserRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(1);
    }

    @Test
    void resetPasswordShouldChangePassword() {
        userService.resetPassword(testUser.getId(), "newpassword123");
        var updated = sysUserRepository.findById(testUser.getId()).orElseThrow();
        assertThat(passwordEncoder.matches("newpassword123", updated.getPasswordHash())).isTrue();
    }
}
