package com.aicoding.warehouse.stock.domain;

import java.util.Optional;

public interface StockRepository {

    Optional<Stock> findBySkuLocation(Long productId, Long warehouseId, Long areaId, Long locationId);

    Stock save(Stock stock);
}
