package com.aicoding.warehouse.inbound.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.inbound.domain.InboundOrderService;
import com.aicoding.warehouse.inbound.infra.InboundOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inbound-orders")
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;

    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    @GetMapping
    public ApiResponse<PageResult<InboundOrderEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        if (pageSize > 100) pageSize = 100;
        Page<InboundOrderEntity> result = inboundOrderService.list(status, warehouseId, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<InboundOrderEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(inboundOrderService.getById(id));
    }

    @PostMapping
    public ApiResponse<InboundOrderEntity> create(@RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(req.warehouseId());
        order.setSupplierId(req.supplierId());
        order.setInboundType(req.inboundType());
        order.setRemark(req.remark());
        return ApiResponse.ok(inboundOrderService.create(order, req.items(), userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<InboundOrderEntity> update(@PathVariable Long id, @RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        InboundOrderEntity order = new InboundOrderEntity();
        order.setWarehouseId(req.warehouseId());
        order.setSupplierId(req.supplierId());
        order.setInboundType(req.inboundType());
        order.setRemark(req.remark());
        return ApiResponse.ok(inboundOrderService.update(id, order, req.items(), userId));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Void> submit(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inboundOrderService.submit(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/audit")
    public ApiResponse<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body, @AuthenticationPrincipal Long userId) {
        inboundOrderService.audit(id, body.get("status"), body.get("auditRemark"), userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirm(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inboundOrderService.confirm(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inboundOrderService.cancel(id, userId);
        return ApiResponse.ok();
    }

    record CreateRequest(Long warehouseId, Long supplierId, String inboundType, String remark,
                         List<InboundOrderService.InboundOrderItemDTO> items) {}
}