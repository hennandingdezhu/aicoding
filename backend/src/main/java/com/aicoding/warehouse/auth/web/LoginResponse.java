package com.aicoding.warehouse.auth.web;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserInfo user
) {
    public record UserInfo(Long id, String username, String realName) {}
}
