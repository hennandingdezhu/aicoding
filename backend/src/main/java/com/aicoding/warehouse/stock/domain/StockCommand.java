package com.aicoding.warehouse.stock.domain;

import java.math.BigDecimal;

public interface StockCommand {

    Long productId();

    Long warehouseId();

    Long areaId();

    Long locationId();

    BigDecimal quantity();
}
