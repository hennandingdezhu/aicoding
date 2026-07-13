package com.aicoding.warehouse.stock.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class StockLog {

    private Long id;
    private String logNo;
    private Long productId;
    private Long warehouseId;
    private Long areaId;
    private Long locationId;
    private String changeType;
    private BigDecimal beforeQuantity;
    private BigDecimal changeQuantity;
    private BigDecimal afterQuantity;
    private String businessNo;
    private Long operatedBy;
    private LocalDateTime operatedAt;

    public StockLog() {}

    private StockLog(
            String changeType,
            Stock stock,
            BigDecimal beforeQuantity,
            BigDecimal changeQuantity,
            BigDecimal afterQuantity,
            String businessNo,
            Long operatedBy
    ) {
        this.logNo = "SL-" + UUID.randomUUID();
        this.productId = stock.getProductId();
        this.warehouseId = stock.getWarehouseId();
        this.areaId = stock.getAreaId();
        this.locationId = stock.getLocationId();
        this.changeType = changeType;
        this.beforeQuantity = beforeQuantity;
        this.changeQuantity = changeQuantity;
        this.afterQuantity = afterQuantity;
        this.businessNo = businessNo;
        this.operatedBy = operatedBy;
        this.operatedAt = LocalDateTime.now();
    }

    public static StockLog of(
            String changeType,
            Stock stock,
            BigDecimal beforeQuantity,
            BigDecimal changeQuantity,
            BigDecimal afterQuantity,
            String businessNo,
            Long operatedBy
    ) {
        return new StockLog(changeType, stock, beforeQuantity, changeQuantity, afterQuantity, businessNo, operatedBy);
    }

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
    public String getBusinessNo() { return businessNo; }
    public void setBusinessNo(String businessNo) { this.businessNo = businessNo; }
    public Long getOperatedBy() { return operatedBy; }
    public void setOperatedBy(Long operatedBy) { this.operatedBy = operatedBy; }
    public LocalDateTime getOperatedAt() { return operatedAt; }
    public void setOperatedAt(LocalDateTime operatedAt) { this.operatedAt = operatedAt; }
}
