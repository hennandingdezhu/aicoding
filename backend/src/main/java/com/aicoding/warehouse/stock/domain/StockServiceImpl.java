package com.aicoding.warehouse.stock.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockLogRepository stockLogRepository;

    public StockServiceImpl(StockRepository stockRepository, StockLogRepository stockLogRepository) {
        this.stockRepository = stockRepository;
        this.stockLogRepository = stockLogRepository;
    }

    @Override
    public void confirmInbound(StockCommand command, String businessNo, Long operatedBy) {
        Stock stock = getOrCreateStock(command);
        BigDecimal beforeQuantity = stock.getTotalQuantity();
        stock.increaseAvailable(command.quantity());
        stockRepository.save(stock);
        writeLog("INBOUND", stock, beforeQuantity, command.quantity(), stock.getTotalQuantity(), businessNo, operatedBy);
    }

    @Override
    public void lockOutbound(StockCommand command, String businessNo, Long operatedBy) {
        Stock stock = getExistingStock(command);
        BigDecimal beforeQuantity = stock.getTotalQuantity();
        stock.lock(command.quantity());
        stockRepository.save(stock);
        writeLog("OUTBOUND_LOCK", stock, beforeQuantity, BigDecimal.ZERO, stock.getTotalQuantity(), businessNo, operatedBy);
    }

    @Override
    public void confirmOutbound(StockCommand command, String businessNo, Long operatedBy) {
        Stock stock = getExistingStock(command);
        BigDecimal beforeQuantity = stock.getTotalQuantity();
        stock.confirmLockedOutbound(command.quantity());
        stockRepository.save(stock);
        writeLog("OUTBOUND", stock, beforeQuantity, command.quantity().negate(), stock.getTotalQuantity(), businessNo, operatedBy);
    }

    @Override
    public void cancelOutbound(StockCommand command, String businessNo, Long operatedBy) {
        Stock stock = getExistingStock(command);
        BigDecimal beforeQuantity = stock.getTotalQuantity();
        stock.releaseLocked(command.quantity());
        stockRepository.save(stock);
        writeLog("OUTBOUND_CANCEL", stock, beforeQuantity, BigDecimal.ZERO, stock.getTotalQuantity(), businessNo, operatedBy);
    }

    @Override
    public void adjustOutbound(StockCommand command, String businessNo, Long operatedBy) {
        Stock stock = getExistingStock(command);
        BigDecimal beforeQuantity = stock.getTotalQuantity();
        stock.decreaseAvailable(command.quantity());
        stockRepository.save(stock);
        writeLog("CHECK_ADJUST", stock, beforeQuantity, command.quantity().negate(), stock.getTotalQuantity(), businessNo, operatedBy);
    }

    private Stock getOrCreateStock(StockCommand command) {
        return stockRepository.findBySkuLocation(
                command.productId(),
                command.warehouseId(),
                command.areaId(),
                command.locationId()
        ).orElseGet(() -> new Stock(command.productId(), command.warehouseId(), command.areaId(), command.locationId()));
    }

    private Stock getExistingStock(StockCommand command) {
        return stockRepository.findBySkuLocation(
                command.productId(),
                command.warehouseId(),
                command.areaId(),
                command.locationId()
        ).orElseThrow(() -> new StockShortageException("库存不存在"));
    }

    private void writeLog(
            String changeType,
            Stock stock,
            BigDecimal beforeQuantity,
            BigDecimal changeQuantity,
            BigDecimal afterQuantity,
            String businessNo,
            Long operatedBy
    ) {
        stockLogRepository.save(StockLog.of(
                changeType,
                stock,
                beforeQuantity,
                changeQuantity,
                afterQuantity,
                businessNo,
                operatedBy
        ));
    }
}
