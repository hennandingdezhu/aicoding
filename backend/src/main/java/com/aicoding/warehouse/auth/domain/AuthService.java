package com.aicoding.warehouse.auth.domain;

import com.aicoding.warehouse.auth.web.LoginRequest;
import com.aicoding.warehouse.auth.web.LoginResponse;
import com.aicoding.warehouse.auth.web.ChangePasswordRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout(Long userId);
    LoginResponse.UserInfo me(Long userId);
    void changePassword(Long userId, ChangePasswordRequest request);
}
