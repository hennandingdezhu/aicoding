package com.aicoding.warehouse.role.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.role.domain.RoleService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final SysRoleRepository sysRoleRepository;
    private final SysRolePermissionRepository sysRolePermissionRepository;

    public RoleServiceImpl(SysRoleRepository sysRoleRepository,
                           SysRolePermissionRepository sysRolePermissionRepository) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysRolePermissionRepository = sysRolePermissionRepository;
    }

    @Override
    public PageResult<RoleInfo> listRoles(String keyword, Pageable pageable) {
        Specification<SysRole> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("roleName"), like),
                        cb.like(root.get("roleCode"), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<SysRole> page = sysRoleRepository.findAll(spec, pageable);
        List<RoleInfo> records = page.getContent().stream()
                .map(this::toInfo)
                .toList();
        return new PageResult<>(records, page.getTotalElements(), pageable.getPageNumber() + 1, pageable.getPageSize());
    }

    @Override
    @Transactional
    public RoleInfo createRole(CreateRoleCommand command) {
        if (sysRoleRepository.existsByRoleCodeAndDeleted(command.roleCode(), 0)) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        role.setRoleCode(command.roleCode());
        role.setRoleName(command.roleName());
        role.setDescription(command.description());
        role.setStatus(1);
        SysRole saved = sysRoleRepository.save(role);
        return toInfo(saved);
    }

    @Override
    @Transactional
    public RoleInfo updateRole(Long id, UpdateRoleCommand command) {
        SysRole role = sysRoleRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        role.setRoleName(command.roleName());
        if (command.description() != null) {
            role.setDescription(command.description());
        }
        return toInfo(sysRoleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        SysRole role = sysRoleRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        role.setDeleted(1);
        sysRoleRepository.save(role);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, AssignPermissionsCommand command) {
        sysRoleRepository.findByIdAndDeleted(roleId, 0)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        sysRolePermissionRepository.deleteByRoleId(roleId);
        List<SysRolePermission> mappings = command.permissionIds().stream()
                .map(permId -> new SysRolePermission(roleId, permId))
                .toList();
        sysRolePermissionRepository.saveAll(mappings);
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return sysRolePermissionRepository.findByRoleId(roleId).stream()
                .map(SysRolePermission::getPermissionId)
                .toList();
    }

    private RoleInfo toInfo(SysRole role) {
        return new RoleInfo(role.getId(), role.getRoleCode(), role.getRoleName(),
                role.getDescription(), role.getStatus(), role.getCreatedAt(), role.getUpdatedAt());
    }
}
