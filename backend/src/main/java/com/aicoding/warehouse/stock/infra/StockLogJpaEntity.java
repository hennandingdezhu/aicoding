package com.aicoding.warehouse.stock.infra;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_log")
public class StockLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_no", length = 64, nullable = false, unique = true)
    private String logNo;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "change_type", length = 32)
    private String changeType;

    @Column(name = "before_quantity", precision = 18, scale = 4)
    private BigDecimal beforeQuantity;

    @Column(name = "change_quantity", precision = 18, scale = 4)
    private BigDecimal changeQuantity;

    @Column(name = "after_quantity", precision = 18, scale = 4)
    private BigDecimal afterQuantity;

    @Column(name = "business_type", length = 64)
    private String businessType;

    @Column(name = "business_id")
    private Long businessId;

    @Column(name = "business_no", length = 64)
    private String businessNo;

    @Column(name = "operated_by")
    private Long operatedBy;

    @Column(name = "operated_at")
    private LocalDateTime operatedAt;

    @Column(name = "remark", length = 500)
    private String remark;

    public StockLogJpaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogNo() { return logNo; }
    public void setLogNo(String logNo) { this.logNo = logNo; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public BigDecimal getBeforeQuantity() { return beforeQuantity; }
    public void setBeforeQuantity(BigDecimal beforeQuantity) { this.beforeQuantity = beforeQuantity; }
    public BigDecimal getChangeQuantity() { return changeQuantity; }
    public void setChangeQuantity(BigDecimal changeQuantity) { this.changeQuantity = changeQuantity; }
    public BigDecimal getAfterQuantity() { return afterQuantity; }
    public void setAfterQuantity(BigDecimal afterQuantity) { this.afterQuantity = afterQuantity; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public Long getBusinessId() { return businessId; }
    public void setBusinessId(Long businessId) { this.businessId = businessId; }
    public String getBusinessNo() { return businessNo; }
    public void setBusinessNo(String businessNo) { this.businessNo = businessNo; }
    public Long getOperatedBy() { return operatedBy; }
    public void setOperatedBy(Long operatedBy) { this.operatedBy = operatedBy; }
    public LocalDateTime getOperatedAt() { return operatedAt; }
    public void setOperatedAt(LocalDateTime operatedAt) { this.operatedAt = operatedAt; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}