package com.aicoding.warehouse.warehousearea.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_area", uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "area_code"}))
public class WarehouseArea {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    @Column(name = "area_code", nullable = false, length = 64)
    private String areaCode;
    @Column(name = "area_name", nullable = false, length = 128)
    private String areaName;
    @Column(nullable = false)
    private Integer status = 1;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private Integer deleted = 0;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); if (status == null) status = 1; if (deleted == null) deleted = 0; }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
