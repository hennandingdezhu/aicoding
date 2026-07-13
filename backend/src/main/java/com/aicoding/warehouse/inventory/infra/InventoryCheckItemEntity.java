package com.aicoding.warehouse.inventory.infra;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_check_item")
public class InventoryCheckItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_check_id")
    private InventoryCheckEntity inventoryCheck;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "book_quantity", precision = 18, scale = 4)
    private BigDecimal bookQuantity;

    @Column(name = "actual_quantity", precision = 18, scale = 4)
    private BigDecimal actualQuantity;

    @Column(name = "difference_quantity", precision = 18, scale = 4)
    private BigDecimal differenceQuantity;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InventoryCheckItemEntity() {}
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public InventoryCheckEntity getInventoryCheck() { return inventoryCheck; }
    public void setInventoryCheck(InventoryCheckEntity inventoryCheck) { this.inventoryCheck = inventoryCheck; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public BigDecimal getBookQuantity() { return bookQuantity; }
    public void setBookQuantity(BigDecimal bookQuantity) { this.bookQuantity = bookQuantity; }
    public BigDecimal getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(BigDecimal actualQuantity) { this.actualQuantity = actualQuantity; }
    public BigDecimal getDifferenceQuantity() { return differenceQuantity; }
    public void setDifferenceQuantity(BigDecimal differenceQuantity) { this.differenceQuantity = differenceQuantity; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}