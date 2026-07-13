package com.aicoding.warehouse.inventory;

import com.aicoding.warehouse.inventory.domain.InventoryCheckService;
import com.aicoding.warehouse.inventory.infra.InventoryCheckEntity;
import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockRepository;
import com.aicoding.warehouse.stock.domain.StockService;
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
class InventoryCheckIntegrationTest {

    @Autowired
    private InventoryCheckService inventoryCheckService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 100L;

    @Test
    void createCheckShouldAutoGenerateItemsFromStock() {
        stockService.confirmInbound(
                new StockCmd(PRODUCT_ID, 1L, 1L, 1L, new BigDecimal("50")), "IN-001", USER_ID);

        InventoryCheckEntity check = inventoryCheckService.create(1L, 1L, null, "monthly check", USER_ID);
        assertThat(check.getStatus()).isEqualTo("DRAFT");
        assertThat(check.getOrderNo()).startsWith("IC-");
        assertThat(check.getItems()).isNotEmpty();
        assertThat(check.getItems().get(0).getBookQuantity()).isEqualByComparingTo("50");
    }

    @Test
    void fullCheckFlowWithAdjustmentShouldUpdateStock() {
        stockService.confirmInbound(
                new StockCmd(PRODUCT_ID, 1L, 1L, 1L, new BigDecimal("50")), "IN-001", USER_ID);

        InventoryCheckEntity check = inventoryCheckService.create(1L, 1L, null, "test", USER_ID);
        Long itemId = check.getItems().get(0).getId();

        // Enter actual count: 48 (short 2)
        inventoryCheckService.updateItems(check.getId(),
                List.of(new InventoryCheckService.CheckItemUpdate(itemId, new BigDecimal("48"), "short 2")), USER_ID);

        check = inventoryCheckService.getById(check.getId());
        assertThat(check.getItems().get(0).getActualQuantity()).isEqualByComparingTo("48");
        assertThat(check.getItems().get(0).getDifferenceQuantity()).isEqualByComparingTo("-2");

        inventoryCheckService.submit(check.getId(), USER_ID);
        inventoryCheckService.audit(check.getId(), "APPROVED", null, USER_ID);
        inventoryCheckService.adjust(check.getId(), USER_ID);

        assertThat(inventoryCheckService.getById(check.getId()).getStatus()).isEqualTo("ADJUSTED");

        // Stock should be adjusted: 50 - 2 = 48
        Stock stock = stockRepository.findBySkuLocation(PRODUCT_ID, 1L, 1L, 1L).orElseThrow();
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("48");
    }

    @Test
    void surplusAdjustmentShouldIncreaseStock() {
        stockService.confirmInbound(
                new StockCmd(PRODUCT_ID, 1L, 1L, 1L, new BigDecimal("50")), "IN-001", USER_ID);

        InventoryCheckEntity check = inventoryCheckService.create(1L, 1L, null, "test", USER_ID);
        Long itemId = check.getItems().get(0).getId();

        // Actual: 55 (surplus 5)
        inventoryCheckService.updateItems(check.getId(),
                List.of(new InventoryCheckService.CheckItemUpdate(itemId, new BigDecimal("55"), "surplus 5")), USER_ID);

        inventoryCheckService.submit(check.getId(), USER_ID);
        inventoryCheckService.audit(check.getId(), "APPROVED", null, USER_ID);
        inventoryCheckService.adjust(check.getId(), USER_ID);

        Stock stock = stockRepository.findBySkuLocation(PRODUCT_ID, 1L, 1L, 1L).orElseThrow();
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("55");
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}