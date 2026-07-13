package com.aicoding.warehouse.stock;

import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockLog;
import com.aicoding.warehouse.stock.domain.StockLogRepository;
import com.aicoding.warehouse.stock.domain.StockRepository;
import com.aicoding.warehouse.stock.domain.StockService;
import com.aicoding.warehouse.stock.domain.StockServiceImpl;
import com.aicoding.warehouse.stock.domain.StockShortageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockDomainServiceTest {

    private StockRepository stockRepository;
    private StockLogRepository stockLogRepository;
    private StockService stockService;

    @BeforeEach
    void setUp() {
        stockRepository = new InMemoryStockRepository();
        stockLogRepository = new InMemoryStockLogRepository();
        stockService = new StockServiceImpl(stockRepository, stockLogRepository);
    }

    @Test
    void confirmInboundShouldIncreaseTotalAndAvailableQuantityAndWriteLog() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("10")), "IN-001", 100L);

        Stock stock = ((InMemoryStockRepository) stockRepository).findRequired(1L, 1L, 1L, 1L);
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("10");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("10");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");

        List<StockLog> logs = ((InMemoryStockLogRepository) stockLogRepository).findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.getFirst().getChangeType()).isEqualTo("INBOUND");
        assertThat(logs.getFirst().getBeforeQuantity()).isEqualByComparingTo("0");
        assertThat(logs.getFirst().getChangeQuantity()).isEqualByComparingTo("10");
        assertThat(logs.getFirst().getAfterQuantity()).isEqualByComparingTo("10");
    }

    @Test
    void approveOutboundShouldMoveAvailableQuantityToLockedQuantity() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("10")), "IN-001", 100L);

        stockService.lockOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 101L);

        Stock stock = ((InMemoryStockRepository) stockRepository).findRequired(1L, 1L, 1L, 1L);
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("10");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("6");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("4");
    }

    @Test
    void approveOutboundShouldFailWhenAvailableQuantityIsInsufficient() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("3")), "IN-001", 100L);

        assertThatThrownBy(() -> stockService.lockOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 101L))
                .isInstanceOf(StockShortageException.class)
                .hasMessageContaining("可用库存不足");
    }

    @Test
    void confirmOutboundShouldReduceTotalAndLockedQuantityAndWriteLog() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("10")), "IN-001", 100L);
        stockService.lockOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 101L);

        stockService.confirmOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 102L);

        Stock stock = ((InMemoryStockRepository) stockRepository).findRequired(1L, 1L, 1L, 1L);
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("6");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("6");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");

        assertThat(((InMemoryStockLogRepository) stockLogRepository).findAll())
                .extracting(StockLog::getChangeType)
                .containsExactly("INBOUND", "OUTBOUND_LOCK", "OUTBOUND");
    }

    @Test
    void cancelOutboundShouldReleaseLockedQuantityBackToAvailableQuantity() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("10")), "IN-001", 100L);
        stockService.lockOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 101L);

        stockService.cancelOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "OUT-001", 102L);

        Stock stock = ((InMemoryStockRepository) stockRepository).findRequired(1L, 1L, 1L, 1L);
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("10");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("10");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");
    }

    @Test
    void adjustOutboundShouldDirectlyReduceTotalAndAvailableWithoutLocking() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("10")), "IN-001", 100L);

        stockService.adjustOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("3")), "IC-001", 200L);

        Stock stock = ((InMemoryStockRepository) stockRepository).findRequired(1L, 1L, 1L, 1L);
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("7");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("7");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");

        List<StockLog> logs = ((InMemoryStockLogRepository) stockLogRepository).findAll();
        assertThat(logs).extracting(StockLog::getChangeType)
                .containsExactly("INBOUND", "CHECK_ADJUST");
        StockLog adjustLog = logs.get(1);
        assertThat(adjustLog.getChangeQuantity()).isEqualByComparingTo("-3");
        assertThat(adjustLog.getAfterQuantity()).isEqualByComparingTo("7");
    }

    @Test
    void adjustOutboundShouldFailWhenAvailableQuantityIsInsufficient() {
        stockService.confirmInbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("3")), "IN-001", 100L);

        assertThatThrownBy(() -> stockService.adjustOutbound(new StockCommand(1L, 1L, 1L, 1L, new BigDecimal("4")), "IC-001", 200L))
                .isInstanceOf(StockShortageException.class)
                .hasMessageContaining("可用库存不足");
    }

    @Test
    void decreaseAvailableShouldReduceTotalAndAvailableQuantity() {
        Stock stock = new Stock(1L, 1L, 1L, 1L);
        stock.increaseAvailable(new BigDecimal("10"));
        stock.decreaseAvailable(new BigDecimal("3"));

        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("7");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("7");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");
    }

    @Test
    void decreaseAvailableShouldFailWhenAvailableIsInsufficient() {
        Stock stock = new Stock(1L, 1L, 1L, 1L);
        stock.increaseAvailable(new BigDecimal("3"));

        assertThatThrownBy(() -> stock.decreaseAvailable(new BigDecimal("4")))
                .isInstanceOf(StockShortageException.class)
                .hasMessageContaining("可用库存不足");
    }

    record StockCommand(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity)
            implements com.aicoding.warehouse.stock.domain.StockCommand {
    }
}
