package com.aicoding.warehouse.user.domain;

import com.aicoding.warehouse.common.PageResult;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    PageResult<UserInfo> listUsers(String keyword, Integer status, Pageable pageable);
    UserDetail getUserDetail(Long id);
    UserInfo createUser(CreateUserCommand command);
    UserInfo updateUser(Long id, UpdateUserCommand command);
    void deleteUser(Long id);
    void updateUserStatus(Long id, Integer status);
    void resetPassword(Long id, String newPassword);

    record UserInfo(Long id, String username, String realName, String phone, String email,
                    Integer status, java.time.LocalDateTime createdAt) {}
    record UserDetail(Long id, String username, String realName, String phone, String email,
                      Integer status, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt,
                      List<Long> roleIds, List<Long> warehouseIds) {}
    record CreateUserCommand(String username, String password, String realName, String phone, String email,
                             List<Long> roleIds, List<Long> warehouseIds) {}
    record UpdateUserCommand(String realName, String phone, String email,
                             List<Long> roleIds, List<Long> warehouseIds) {}
}
