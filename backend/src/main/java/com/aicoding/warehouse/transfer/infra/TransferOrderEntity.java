package com.aicoding.warehouse.transfer.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transfer_order")
public class TransferOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", length = 64, nullable = false, unique = true)
    private String orderNo;

    @Column(name = "transfer_type", length = 32)
    private String transferType;

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

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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

    @OneToMany(mappedBy = "transferOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransferOrderItemEntity> items = new ArrayList<>();

    public TransferOrderEntity() {}
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
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
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
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
    public List<TransferOrderItemEntity> getItems() { return items; }
    public void setItems(List<TransferOrderItemEntity> items) { this.items = items; }
    public void addItem(TransferOrderItemEntity item) { items.add(item); item.setTransferOrder(this); }
}