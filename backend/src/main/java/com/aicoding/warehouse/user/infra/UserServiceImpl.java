package com.aicoding.warehouse.user.infra;

import com.aicoding.warehouse.auth.infra.SysUser;
import com.aicoding.warehouse.auth.infra.SysUserRepository;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.user.domain.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysUserWarehouseRepository sysUserWarehouseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SysUserRepository sysUserRepository,
                           SysUserRoleRepository sysUserRoleRepository,
                           SysUserWarehouseRepository sysUserWarehouseRepository,
                           PasswordEncoder passwordEncoder) {
        this.sysUserRepository = sysUserRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysUserWarehouseRepository = sysUserWarehouseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResult<UserInfo> listUsers(String keyword, Integer status, Pageable pageable) {
        Specification<SysUser> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("username"), like),
                        cb.like(root.get("realName"), like),
                        cb.like(root.get("phone"), like)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<SysUser> page = sysUserRepository.findAll(spec, pageable);
        List<UserInfo> records = page.getContent().stream()
                .map(u -> new UserInfo(u.getId(), u.getUsername(), u.getRealName(), u.getPhone(),
                        u.getEmail(), u.getStatus(), u.getCreatedAt()))
                .toList();
        return new PageResult<>(records, page.getTotalElements(), pageable.getPageNumber() + 1, pageable.getPageSize());
    }

    @Override
    public UserDetail getUserDetail(Long id) {
        SysUser user = sysUserRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        List<Long> roleIds = sysUserRoleRepository.findByUserId(id).stream()
                .map(SysUserRole::getRoleId).toList();
        List<Long> warehouseIds = sysUserWarehouseRepository.findByUserId(id).stream()
                .map(SysUserWarehouse::getWarehouseId).toList();
        return new UserDetail(user.getId(), user.getUsername(), user.getRealName(), user.getPhone(),
                user.getEmail(), user.getStatus(), user.getCreatedAt(), user.getUpdatedAt(),
                roleIds, warehouseIds);
    }

    @Override
    @Transactional
    public UserInfo createUser(CreateUserCommand command) {
        if (sysUserRepository.findByUsernameAndDeleted(command.username(), 0).isPresent()) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(command.username());
        user.setPasswordHash(passwordEncoder.encode(command.password()));
        user.setRealName(command.realName());
        user.setPhone(command.phone());
        user.setEmail(command.email());
        user.setStatus(1);
        SysUser saved = sysUserRepository.save(user);

        if (command.roleIds() != null && !command.roleIds().isEmpty()) {
            List<SysUserRole> roles = command.roleIds().stream()
                    .map(rid -> new SysUserRole(saved.getId(), rid))
                    .toList();
            sysUserRoleRepository.saveAll(roles);
        }
        if (command.warehouseIds() != null && !command.warehouseIds().isEmpty()) {
            List<SysUserWarehouse> warehouses = command.warehouseIds().stream()
                    .map(wid -> new SysUserWarehouse(saved.getId(), wid))
                    .toList();
            sysUserWarehouseRepository.saveAll(warehouses);
        }
        return toInfo(saved);
    }

    @Override
    @Transactional
    public UserInfo updateUser(Long id, UpdateUserCommand command) {
        SysUser user = sysUserRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setRealName(command.realName());
        user.setPhone(command.phone());
        user.setEmail(command.email());
        SysUser saved = sysUserRepository.save(user);

        sysUserRoleRepository.deleteByUserId(id);
        if (command.roleIds() != null && !command.roleIds().isEmpty()) {
            List<SysUserRole> roles = command.roleIds().stream()
                    .map(rid -> new SysUserRole(id, rid))
                    .toList();
            sysUserRoleRepository.saveAll(roles);
        }

        sysUserWarehouseRepository.deleteByUserId(id);
        if (command.warehouseIds() != null && !command.warehouseIds().isEmpty()) {
            List<SysUserWarehouse> warehouses = command.warehouseIds().stream()
                    .map(wid -> new SysUserWarehouse(id, wid))
                    .toList();
            sysUserWarehouseRepository.saveAll(warehouses);
        }
        return toInfo(saved);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        SysUser user = sysUserRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setDeleted(1);
        sysUserRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, Integer status) {
        SysUser user = sysUserRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setStatus(status);
        sysUserRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        SysUser user = sysUserRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        sysUserRepository.save(user);
    }

    private UserInfo toInfo(SysUser user) {
        return new UserInfo(user.getId(), user.getUsername(), user.getRealName(), user.getPhone(),
                user.getEmail(), user.getStatus(), user.getCreatedAt());
    }
}
