package com.aicoding.warehouse.transfer.infra;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_order_item")
public class TransferOrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_order_id")
    private TransferOrderEntity transferOrder;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "from_warehouse_id")
    private Long fromWarehouseId;

    @Column(name = "from_area_id")
    private Long fromAreaId;

    @Column(name = "from_location_id")
    private Long fromLocationId;

    @Column(name = "to_warehouse_id")
    private Long toWarehouseId;

    @Column(name = "to_area_id")
    private Long toAreaId;

    @Column(name = "to_location_id")
    private Long toLocationId;

    @Column(name = "quantity", precision = 18, scale = 4)
    private BigDecimal quantity;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public TransferOrderItemEntity() {}
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TransferOrderEntity getTransferOrder() { return transferOrder; }
    public void setTransferOrder(TransferOrderEntity transferOrder) { this.transferOrder = transferOrder; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getFromWarehouseId() { return fromWarehouseId; }
    public void setFromWarehouseId(Long fromWarehouseId) { this.fromWarehouseId = fromWarehouseId; }
    public Long getFromAreaId() { return fromAreaId; }
    public void setFromAreaId(Long fromAreaId) { this.fromAreaId = fromAreaId; }
    public Long getFromLocationId() { return fromLocationId; }
    public void setFromLocationId(Long fromLocationId) { this.fromLocationId = fromLocationId; }
    public Long getToWarehouseId() { return toWarehouseId; }
    public void setToWarehouseId(Long toWarehouseId) { this.toWarehouseId = toWarehouseId; }
    public Long getToAreaId() { return toAreaId; }
    public void setToAreaId(Long toAreaId) { this.toAreaId = toAreaId; }
    public Long getToLocationId() { return toLocationId; }
    public void setToLocationId(Long toLocationId) { this.toLocationId = toLocationId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}