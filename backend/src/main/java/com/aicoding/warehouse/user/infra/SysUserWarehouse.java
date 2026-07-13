package com.aicoding.warehouse.user.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user_warehouse", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "warehouse_id"}))
public class SysUserWarehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public SysUserWarehouse() {}
    public SysUserWarehouse(Long userId, Long warehouseId) { this.userId = userId; this.warehouseId = warehouseId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
