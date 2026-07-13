package com.aicoding.warehouse.user.web;

import java.util.List;

public record UpdateUserRequest(
        String realName,
        String phone,
        String email,
        List<Long> roleIds,
        List<Long> warehouseIds
) {}
