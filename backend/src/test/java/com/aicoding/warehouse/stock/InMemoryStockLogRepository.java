package com.aicoding.warehouse.stock;

import com.aicoding.warehouse.stock.domain.StockLog;
import com.aicoding.warehouse.stock.domain.StockLogRepository;

import java.util.ArrayList;
import java.util.List;

class InMemoryStockLogRepository implements StockLogRepository {

    private final List<StockLog> logs = new ArrayList<>();

    @Override
    public StockLog save(StockLog stockLog) {
        logs.add(stockLog);
        return stockLog;
    }

    List<StockLog> findAll() {
        return logs;
    }
}
