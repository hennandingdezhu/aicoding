package com.aicoding.warehouse.stock.domain;

public interface StockService {

    void confirmInbound(StockCommand command, String businessNo, Long operatedBy);

    void lockOutbound(StockCommand command, String businessNo, Long operatedBy);

    void confirmOutbound(StockCommand command, String businessNo, Long operatedBy);

    void cancelOutbound(StockCommand command, String businessNo, Long operatedBy);

    void adjustOutbound(StockCommand command, String businessNo, Long operatedBy);
}
