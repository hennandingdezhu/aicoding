package com.aicoding.warehouse.role.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_role_permission", uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "permission_id"}))
public class SysRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public SysRolePermission() {}
    public SysRolePermission(Long roleId, Long permissionId) { this.roleId = roleId; this.permissionId = permissionId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getPermissionId() { return permissionId; }
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
