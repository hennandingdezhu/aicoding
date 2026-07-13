package com.aicoding.warehouse.transfer;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockRepository;
import com.aicoding.warehouse.stock.domain.StockService;
import com.aicoding.warehouse.transfer.domain.TransferOrderService;
import com.aicoding.warehouse.transfer.infra.TransferOrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransferOrderIntegrationTest {

    @Autowired
    private TransferOrderService transferOrderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 100L;

    private void prepareStock(Long warehouseId, Long areaId, Long locationId, BigDecimal qty) {
        stockService.confirmInbound(
                new StockCmd(PRODUCT_ID, warehouseId, areaId, locationId, qty), "IN-SETUP", USER_ID);
    }

    @Test
    void executeTransferShouldMoveStockBetweenLocations() {
        prepareStock(1L, 1L, 1L, new BigDecimal("100"));

        TransferOrderEntity order = new TransferOrderEntity();
        order.setTransferType("LOCATION");
        var items = List.of(new TransferOrderService.TransferOrderItemDTO(
                PRODUCT_ID, 1L, 1L, 1L, 1L, 2L, 2L, new BigDecimal("30"), null));
        TransferOrderEntity created = transferOrderService.create(order, items, USER_ID);
        transferOrderService.submit(created.getId(), USER_ID);
        transferOrderService.audit(created.getId(), "APPROVED", null, USER_ID);
        transferOrderService.execute(created.getId(), USER_ID);

        assertThat(transferOrderService.getById(created.getId()).getStatus()).isEqualTo("COMPLETED");

        // Source stock decreased
        Stock source = stockRepository.findBySkuLocation(PRODUCT_ID, 1L, 1L, 1L).orElseThrow();
        assertThat(source.getTotalQuantity()).isEqualByComparingTo("70");

        // Target stock increased
        Stock target = stockRepository.findBySkuLocation(PRODUCT_ID, 1L, 2L, 2L).orElseThrow();
        assertThat(target.getTotalQuantity()).isEqualByComparingTo("30");
    }

    @Test
    void sameLocationTransferShouldFail() {
        assertThatThrownBy(() -> {
            TransferOrderEntity order = new TransferOrderEntity();
            order.setTransferType("LOCATION");
            var items = List.of(new TransferOrderService.TransferOrderItemDTO(
                    PRODUCT_ID, 1L, 1L, 1L, 1L, 1L, 1L, new BigDecimal("10"), null));
            transferOrderService.create(order, items, USER_ID);
        }).isInstanceOf(BusinessException.class).hasMessageContaining("相同");
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}