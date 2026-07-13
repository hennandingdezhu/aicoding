package com.aicoding.warehouse.inbound;

import com.aicoding.warehouse.inbound.domain.InboundOrderService;
import com.aicoding.warehouse.inbound.infra.InboundOrderEntity;
import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InboundOrderIntegrationTest {

    @Autowired
    private InboundOrderService inboundOrderService;

    @Autowired
    private StockRepository stockRepository;

    private static final Long USER_ID = 1L;
    private static final Long WAREHOUSE_ID = 1L;
    private static final Long PRODUCT_ID = 100L;
    private static final Long AREA_ID = 1L;
    private static final Long LOCATION_ID = 1L;

    @Test
    void fullInboundFlowShouldCreateOrderAndUpdateStock() {
        // When: create
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setInboundType("PURCHASE");
        var items = List.of(new InboundOrderService.InboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("100"), new BigDecimal("50"), "test"));
        InboundOrderEntity created = inboundOrderService.create(order, items, USER_ID);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getOrderNo()).startsWith("IN-");
        assertThat(created.getStatus()).isEqualTo("DRAFT");
        assertThat(created.getItems()).hasSize(1);

        // When: submit
        inboundOrderService.submit(created.getId(), USER_ID);
        InboundOrderEntity submitted = inboundOrderService.getById(created.getId());
        assertThat(submitted.getStatus()).isEqualTo("PENDING");

        // When: audit approve
        inboundOrderService.audit(created.getId(), "APPROVED", null, USER_ID);
        InboundOrderEntity approved = inboundOrderService.getById(created.getId());
        assertThat(approved.getStatus()).isEqualTo("APPROVED");

        // When: confirm - should update stock
        inboundOrderService.confirm(created.getId(), USER_ID);
        InboundOrderEntity confirmed = inboundOrderService.getById(created.getId());
        assertThat(confirmed.getStatus()).isEqualTo("CONFIRMED");

        // Then: stock should be increased
        Stock stock = stockRepository.findBySkuLocation(PRODUCT_ID, WAREHOUSE_ID, AREA_ID, LOCATION_ID).orElseThrow();
        assertThat(stock.getTotalQuantity()).isEqualByComparingTo("100");
        assertThat(stock.getAvailableQuantity()).isEqualByComparingTo("100");
        assertThat(stock.getLockedQuantity()).isEqualByComparingTo("0");
    }

    @Test
    void rejectInboundShouldNotAffectStock() {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setInboundType("PURCHASE");
        var items = List.of(new InboundOrderService.InboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("50"), BigDecimal.ZERO, null));
        InboundOrderEntity created = inboundOrderService.create(order, items, USER_ID);
        inboundOrderService.submit(created.getId(), USER_ID);
        inboundOrderService.audit(created.getId(), "REJECTED", "reason", USER_ID);

        InboundOrderEntity rejected = inboundOrderService.getById(created.getId());
        assertThat(rejected.getStatus()).isEqualTo("REJECTED");
        assertThat(stockRepository.findBySkuLocation(PRODUCT_ID, WAREHOUSE_ID, AREA_ID, LOCATION_ID)).isEmpty();
    }

    @Test
    void cancelDraftShouldWork() {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setInboundType("PURCHASE");
        var items = List.of(new InboundOrderService.InboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("10"), BigDecimal.ZERO, null));
        InboundOrderEntity created = inboundOrderService.create(order, items, USER_ID);
        inboundOrderService.cancel(created.getId(), USER_ID);

        assertThat(inboundOrderService.getById(created.getId()).getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void editDraftOrderShouldUpdateItems() {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setInboundType("PURCHASE");
        var items1 = List.of(new InboundOrderService.InboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("10"), BigDecimal.ZERO, null));
        InboundOrderEntity created = inboundOrderService.create(order, items1, USER_ID);

        var items2 = List.of(new InboundOrderService.InboundOrderItemDTO(
                200L, AREA_ID, LOCATION_ID, new BigDecimal("20"), BigDecimal.ZERO, null));
        InboundOrderEntity updated = inboundOrderService.update(created.getId(), order, items2, USER_ID);

        assertThat(updated.getItems()).hasSize(1);
        assertThat(updated.getItems().get(0).getProductId()).isEqualTo(200L);
    }

    @Test
    void listShouldReturnPagedResults() {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(WAREHOUSE_ID);
        order.setInboundType("PURCHASE");
        var items = List.of(new InboundOrderService.InboundOrderItemDTO(
                PRODUCT_ID, AREA_ID, LOCATION_ID, new BigDecimal("10"), BigDecimal.ZERO, null));
        inboundOrderService.create(order, items, USER_ID);

        var page = inboundOrderService.list(null, null, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }
}