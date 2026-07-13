package com.aicoding.warehouse.auth.web;

import com.aicoding.warehouse.auth.domain.AuthService;
import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.security.JwtAuthFilter;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal Long userId) {
        authService.logout(userId);
        return ApiResponse.ok();
    }

    @GetMapping("/me")
    public ApiResponse<LoginResponse.UserInfo> me(@AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(authService.me(userId));
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@AuthenticationPrincipal Long userId,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userId, request);
        return ApiResponse.ok();
    }
}
