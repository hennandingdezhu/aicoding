package com.aicoding.warehouse.user.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.user.domain.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<PageResult<UserService.UserInfo>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return ApiResponse.ok(userService.listUsers(keyword, status, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserService.UserDetail> getUserDetail(@PathVariable Long id) {
        return ApiResponse.ok(userService.getUserDetail(id));
    }

    @PostMapping
    public ApiResponse<UserService.UserInfo> createUser(@Valid @RequestBody CreateUserRequest request) {
        var cmd = new UserService.CreateUserCommand(
                request.username(), request.password(), request.realName(),
                request.phone(), request.email(), request.roleIds(), request.warehouseIds());
        return ApiResponse.ok(userService.createUser(cmd));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserService.UserInfo> updateUser(@PathVariable Long id,
                                                         @RequestBody UpdateUserRequest request) {
        var cmd = new UserService.UpdateUserCommand(
                request.realName(), request.phone(), request.email(),
                request.roleIds(), request.warehouseIds());
        return ApiResponse.ok(userService.updateUser(id, cmd));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateUserStatus(@PathVariable Long id,
                                              @RequestBody UpdateUserStatusRequest request) {
        userService.updateUserStatus(id, request.status());
        return ApiResponse.ok();
    }

    @PutMapping("/{id}/password/reset")
    public ApiResponse<Void> resetPassword(@PathVariable Long id,
                                           @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(id, request.newPassword());
        return ApiResponse.ok();
    }
}
