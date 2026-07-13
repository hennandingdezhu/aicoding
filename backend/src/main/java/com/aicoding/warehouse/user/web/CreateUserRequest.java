package com.aicoding.warehouse.user.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "用户名不能为空") String username,
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 64, message = "密码长度需在6-64位之间")
        String password,
        @NotBlank(message = "姓名不能为空") String realName,
        String phone,
        String email,
        List<Long> roleIds,
        List<Long> warehouseIds
) {}