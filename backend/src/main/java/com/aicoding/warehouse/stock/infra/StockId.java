package com.aicoding.warehouse.stock.infra;

import java.io.Serializable;
import java.util.Objects;

public class StockId implements Serializable {
    private Long productId;
    private Long warehouseId;
    private Long areaId;
    private Long locationId;

    public StockId() {}

    public StockId(Long productId, Long warehouseId, Long areaId, Long locationId) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.areaId = areaId;
        this.locationId = locationId;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockId stockId)) return false;
        return Objects.equals(productId, stockId.productId)
                && Objects.equals(warehouseId, stockId.warehouseId)
                && Objects.equals(areaId, stockId.areaId)
                && Objects.equals(locationId, stockId.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, warehouseId, areaId, locationId);
    }
}
