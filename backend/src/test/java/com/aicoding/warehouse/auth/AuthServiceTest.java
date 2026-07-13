package com.aicoding.warehouse.auth;

import com.aicoding.warehouse.auth.domain.AuthService;
import com.aicoding.warehouse.auth.infra.SysUser;
import com.aicoding.warehouse.auth.infra.SysUserRepository;
import com.aicoding.warehouse.auth.web.LoginRequest;
import com.aicoding.warehouse.auth.web.LoginResponse;
import com.aicoding.warehouse.auth.web.ChangePasswordRequest;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private SysUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new SysUser();
        testUser.setUsername("testuser");
        testUser.setPasswordHash(passwordEncoder.encode("123456"));
        testUser.setRealName("测试用户");
        testUser.setStatus(1);
        sysUserRepository.save(testUser);
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreCorrect() {
        LoginRequest request = new LoginRequest("testuser", "123456");
        LoginResponse response = authService.login(request);

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(7200);
        assertThat(response.user().id()).isEqualTo(testUser.getId());
        assertThat(response.user().username()).isEqualTo("testuser");

        var claims = jwtUtils.parseToken(response.accessToken());
        assertThat(jwtUtils.getUsername(claims)).isEqualTo("testuser");
    }

    @Test
    void loginShouldFailWhenPasswordIsWrong() {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户名或密码错误");
    }

    @Test
    void loginShouldFailWhenUserIsDisabled() {
        testUser.setStatus(0);
        sysUserRepository.save(testUser);

        LoginRequest request = new LoginRequest("testuser", "123456");
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户已被禁用");
    }

    @Test
    void loginShouldFailWhenUserNotFound() {
        LoginRequest request = new LoginRequest("nonexistent", "123456");
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户名或密码错误");
    }

    @Test
    void changePasswordShouldSucceedWhenOldPasswordIsCorrect() {
        ChangePasswordRequest request = new ChangePasswordRequest("123456", "newpassword");
        authService.changePassword(testUser.getId(), request);

        SysUser updated = sysUserRepository.findById(testUser.getId()).orElseThrow();
        assertThat(passwordEncoder.matches("newpassword", updated.getPasswordHash())).isTrue();
    }

    @Test
    void changePasswordShouldFailWhenOldPasswordIsWrong() {
        ChangePasswordRequest request = new ChangePasswordRequest("wrong", "newpassword");
        assertThatThrownBy(() -> authService.changePassword(testUser.getId(), request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("原密码错误");
    }
}
