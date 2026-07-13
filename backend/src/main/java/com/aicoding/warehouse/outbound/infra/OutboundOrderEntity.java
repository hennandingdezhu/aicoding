package com.aicoding.warehouse.outbound.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outbound_order")
public class OutboundOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", length = 64, nullable = false, unique = true)
    private String orderNo;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "outbound_type", length = 32)
    private String outboundType;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "audited_by")
    private Long auditedBy;

    @Column(name = "audited_at")
    private LocalDateTime auditedAt;

    @Column(name = "audit_remark", length = 500)
    private String auditRemark;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Integer deleted = 0;

    @OneToMany(mappedBy = "outboundOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OutboundOrderItemEntity> items = new ArrayList<>();

    public OutboundOrderEntity() {}
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getOutboundType() { return outboundType; }
    public void setOutboundType(String outboundType) { this.outboundType = outboundType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public Long getAuditedBy() { return auditedBy; }
    public void setAuditedBy(Long auditedBy) { this.auditedBy = auditedBy; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public List<OutboundOrderItemEntity> getItems() { return items; }
    public void setItems(List<OutboundOrderItemEntity> items) { this.items = items; }
    public void addItem(OutboundOrderItemEntity item) { items.add(item); item.setOutboundOrder(this); }
}