package com.aicoding.warehouse.stock.infra;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@IdClass(StockId.class)
public class StockJpaEntity {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Id
    @Column(name = "area_id")
    private Long areaId;

    @Id
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "total_quantity", precision = 18, scale = 4)
    private BigDecimal totalQuantity;

    @Column(name = "available_quantity", precision = 18, scale = 4)
    private BigDecimal availableQuantity;

    @Column(name = "locked_quantity", precision = 18, scale = 4)
    private BigDecimal lockedQuantity;

    @Column(name = "warning_min", precision = 18, scale = 4)
    private BigDecimal warningMin;

    @Column(name = "warning_max", precision = 18, scale = 4)
    private BigDecimal warningMax;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public StockJpaEntity() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public BigDecimal getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(BigDecimal totalQuantity) { this.totalQuantity = totalQuantity; }
    public BigDecimal getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(BigDecimal availableQuantity) { this.availableQuantity = availableQuantity; }
    public BigDecimal getLockedQuantity() { return lockedQuantity; }
    public void setLockedQuantity(BigDecimal lockedQuantity) { this.lockedQuantity = lockedQuantity; }
    public BigDecimal getWarningMin() { return warningMin; }
    public void setWarningMin(BigDecimal warningMin) { this.warningMin = warningMin; }
    public BigDecimal getWarningMax() { return warningMax; }
    public void setWarningMax(BigDecimal warningMax) { this.warningMax = warningMax; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
