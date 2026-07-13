package com.aicoding.warehouse.stock;

import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryStockRepository implements StockRepository {

    private final Map<String, Stock> stocks = new HashMap<>();

    @Override
    public Optional<Stock> findBySkuLocation(Long productId, Long warehouseId, Long areaId, Long locationId) {
        return Optional.ofNullable(stocks.get(key(productId, warehouseId, areaId, locationId)));
    }

    @Override
    public Stock save(Stock stock) {
        stocks.put(key(stock.getProductId(), stock.getWarehouseId(), stock.getAreaId(), stock.getLocationId()), stock);
        return stock;
    }

    Stock findRequired(Long productId, Long warehouseId, Long areaId, Long locationId) {
        return findBySkuLocation(productId, warehouseId, areaId, locationId).orElseThrow();
    }

    private String key(Long productId, Long warehouseId, Long areaId, Long locationId) {
        return productId + ":" + warehouseId + ":" + areaId + ":" + locationId;
    }
}
