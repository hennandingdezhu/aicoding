package com.aicoding.warehouse.stock.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Stock {

    private Long productId;
    private Long warehouseId;
    private Long areaId;
    private Long locationId;
    private BigDecimal totalQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal lockedQuantity;
    private BigDecimal warningMin;
    private BigDecimal warningMax;

    public Stock() {}

    public Stock(Long productId, Long warehouseId, Long areaId, Long locationId) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.warehouseId = Objects.requireNonNull(warehouseId, "warehouseId must not be null");
        this.areaId = Objects.requireNonNull(areaId, "areaId must not be null");
        this.locationId = Objects.requireNonNull(locationId, "locationId must not be null");
        this.totalQuantity = BigDecimal.ZERO;
        this.availableQuantity = BigDecimal.ZERO;
        this.lockedQuantity = BigDecimal.ZERO;
    }

    public void increaseAvailable(BigDecimal quantity) {
        validatePositive(quantity);
        totalQuantity = totalQuantity.add(quantity);
        availableQuantity = availableQuantity.add(quantity);
    }

    public void decreaseAvailable(BigDecimal quantity) {
        validatePositive(quantity);
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new StockShortageException("可用库存不足");
        }
        availableQuantity = availableQuantity.subtract(quantity);
        totalQuantity = totalQuantity.subtract(quantity);
        if (totalQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new StockShortageException("总库存不足");
        }
    }

    public void lock(BigDecimal quantity) {
        validatePositive(quantity);
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new StockShortageException("可用库存不足");
        }
        availableQuantity = availableQuantity.subtract(quantity);
        lockedQuantity = lockedQuantity.add(quantity);
    }

    public void confirmLockedOutbound(BigDecimal quantity) {
        validatePositive(quantity);
        if (lockedQuantity.compareTo(quantity) < 0) {
            throw new StockShortageException("锁定库存不足");
        }
        lockedQuantity = lockedQuantity.subtract(quantity);
        totalQuantity = totalQuantity.subtract(quantity);
        if (totalQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new StockShortageException("总库存不足");
        }
    }

    public void releaseLocked(BigDecimal quantity) {
        validatePositive(quantity);
        if (lockedQuantity.compareTo(quantity) < 0) {
            throw new StockShortageException("锁定库存不足");
        }
        lockedQuantity = lockedQuantity.subtract(quantity);
        availableQuantity = availableQuantity.add(quantity);
    }

    private void validatePositive(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("库存数量必须大于 0");
        }
    }

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
}
