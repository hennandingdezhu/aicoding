package com.aicoding.warehouse.outbound.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.outbound.domain.OutboundOrderService;
import com.aicoding.warehouse.outbound.infra.OutboundOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/outbound-orders")
public class OutboundOrderController {

    private final OutboundOrderService outboundOrderService;

    public OutboundOrderController(OutboundOrderService outboundOrderService) {
        this.outboundOrderService = outboundOrderService;
    }

    @GetMapping
    public ApiResponse<PageResult<OutboundOrderEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        Page<OutboundOrderEntity> result = outboundOrderService.list(status, warehouseId, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<OutboundOrderEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(outboundOrderService.getById(id));
    }

    @PostMapping
    public ApiResponse<OutboundOrderEntity> create(@RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        OutboundOrderEntity order = new OutboundOrderEntity();
        order.setWarehouseId(req.warehouseId());
        order.setCustomerId(req.customerId());
        order.setOutboundType(req.outboundType());
        order.setRemark(req.remark());
        return ApiResponse.ok(outboundOrderService.create(order, req.items(), userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<OutboundOrderEntity> update(@PathVariable Long id, @RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        OutboundOrderEntity order = new OutboundOrderEntity();
        order.setWarehouseId(req.warehouseId());
        order.setCustomerId(req.customerId());
        order.setOutboundType(req.outboundType());
        order.setRemark(req.remark());
        return ApiResponse.ok(outboundOrderService.update(id, order, req.items(), userId));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Void> submit(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        outboundOrderService.submit(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/audit")
    public ApiResponse<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body, @AuthenticationPrincipal Long userId) {
        outboundOrderService.audit(id, body.get("status"), body.get("auditRemark"), userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirm(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        outboundOrderService.confirm(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        outboundOrderService.cancel(id, userId);
        return ApiResponse.ok();
    }

    record CreateRequest(Long warehouseId, Long customerId, String outboundType, String remark,
                         List<OutboundOrderService.OutboundOrderItemDTO> items) {}
}