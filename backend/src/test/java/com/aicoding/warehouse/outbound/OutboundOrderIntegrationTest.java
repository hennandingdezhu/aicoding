package com.aicoding.warehouse.outbound;

import com.aicoding.warehouse.outbound.domain.OutboundOrderService;
import com.aicoding.warehouse.outbound.infra.OutboundOrderEntity;
import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockService;
import com.aicoding.warehouse.stock.domain.StockShortageException;
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
class OutboundOrderIntegrationTest {

    @Autowired
    private OutboundOrderService outboundOrderService;

    @Autowired
    private StockService stockService;

    private static final Long USER_ID = 1L;
    private static final Long WAREHOUSE_ID = 1L;
    private static final Long PRODUCT_ID = 100L;
    private static final Long AREA_ID = 1L;
    private static final Long LOCATION_ID = 1L;

    private void prepareStock() {
        stockService.confirmInbound(
                new StockCmd(PRODUCT_ID, WAREHOUSE_ID, AREA_ID, LOCATION_ID, new BigDecimal("100")), "IN-SETUP", USER_ID);
    }

    @Test
    void fullOutboundFlowShouldLockThenReduceStock() {
        prepareStock();

        OutboundOrderEntity order = new OutboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setOutboundType("SALE");
        var items = List.of(new OutboundOrderService.OutboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("30"), BigDecimal.ZERO, null));
        OutboundOrderEntity created = outboundOrderService.create(order, items, USER_ID);
        assertThat(created.getStatus()).isEqualTo("DRAFT");

        outboundOrderService.submit(created.getId(), USER_ID);
        assertThat(outboundOrderService.getById(created.getId()).getStatus()).isEqualTo("PENDING");

        // Audit approve should lock stock
        outboundOrderService.audit(created.getId(), "APPROVED", null, USER_ID);
        assertThat(outboundOrderService.getById(created.getId()).getStatus()).isEqualTo("APPROVED");

        // Confirm should consume locked stock
        outboundOrderService.confirm(created.getId(), USER_ID);
        assertThat(outboundOrderService.getById(created.getId()).getStatus()).isEqualTo("CONFIRMED");
    }

    @Test
    void cancelApprovedOutboundShouldReleaseLockedStock() {
        prepareStock();

        OutboundOrderEntity order = new OutboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setOutboundType("SALE");
        var items = List.of(new OutboundOrderService.OutboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("40"), BigDecimal.ZERO, null));
        OutboundOrderEntity created = outboundOrderService.create(order, items, USER_ID);
        outboundOrderService.submit(created.getId(), USER_ID);
        outboundOrderService.audit(created.getId(), "APPROVED", null, USER_ID);

        // Cancel approved - should release lock
        outboundOrderService.cancel(created.getId(), USER_ID);
        assertThat(outboundOrderService.getById(created.getId()).getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void insufficientStockShouldFailAudit() {
        prepareStock();

        OutboundOrderEntity order = new OutboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setOutboundType("SALE");
        var items = List.of(new OutboundOrderService.OutboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("200"), BigDecimal.ZERO, null));
        OutboundOrderEntity created = outboundOrderService.create(order, items, USER_ID);
        outboundOrderService.submit(created.getId(), USER_ID);

        assertThatThrownBy(() -> outboundOrderService.audit(created.getId(), "APPROVED", null, USER_ID))
                .isInstanceOf(StockShortageException.class);
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}