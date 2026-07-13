package com.aicoding.warehouse.user.web;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "用户名不能为空") String username,
        @NotBlank(message = "密码不能为空") String password,
        @NotBlank(message = "姓名不能为空") String realName,
        String phone,
        String email,
        List<Long> roleIds,
        List<Long> warehouseIds
) {}
