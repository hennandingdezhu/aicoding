package com.aicoding.warehouse.auth.infra;

import com.aicoding.warehouse.auth.domain.AuthService;
import com.aicoding.warehouse.auth.web.LoginRequest;
import com.aicoding.warehouse.auth.web.LoginResponse;
import com.aicoding.warehouse.auth.web.ChangePasswordRequest;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // 简单内存限流器，生产环境应使用 Redis
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();

    public AuthServiceImpl(SysUserRepository sysUserRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.sysUserRepository = sysUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String key = request.username();
        int attempts = loginAttempts.getOrDefault(key, 0);
        if (attempts >= 5) {
            throw new BusinessException(429, "登录尝试次数过多，请5分钟后再试");
        }

        SysUser user = sysUserRepository.findByUsernameAndDeleted(request.username(), 0)
                .orElseThrow(() -> {
                    loginAttempts.merge(key, 1, Integer::sum);
                    return new BusinessException(401, "用户名或密码错误");
                });

        if (user.getStatus() != 1) {
            throw new BusinessException(401, "用户已被禁用");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            loginAttempts.merge(key, 1, Integer::sum);
            throw new BusinessException(401, "用户名或密码错误");
        }

        loginAttempts.remove(key);

        user.setLastLoginTime(LocalDateTime.now());
        sysUserRepository.save(user);

        String accessToken = jwtUtils.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(
                accessToken,
                "Bearer",
                7200,
                new LoginResponse.UserInfo(user.getId(), user.getUsername(), user.getRealName())
        );
    }

    @Override
    public void logout(Long userId) {
        // Stateless JWT - no server-side session to invalidate
    }

    @Override
    public LoginResponse.UserInfo me(Long userId) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(401, "用户不存在"));
        return new LoginResponse.UserInfo(user.getId(), user.getUsername(), user.getRealName());
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new BusinessException(400, "原密码错误");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        sysUserRepository.save(user);
    }
}