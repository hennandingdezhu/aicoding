package com.aicoding.warehouse.stock.domain;

public class StockShortageException extends RuntimeException {

    public StockShortageException(String message) {
        super(message);
    }
}
