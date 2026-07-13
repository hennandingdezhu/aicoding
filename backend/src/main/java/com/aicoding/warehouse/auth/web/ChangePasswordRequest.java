package com.aicoding.warehouse.auth.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "原密码不能为空") String oldPassword,
        @NotBlank(message = "新密码不能为空")
        @Size(min = 6, max = 64, message = "新密码长度需在6-64位之间")
        String newPassword
) {}