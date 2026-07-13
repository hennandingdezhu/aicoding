package com.aicoding.warehouse.transfer.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.transfer.domain.TransferOrderService;
import com.aicoding.warehouse.transfer.infra.TransferOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer-orders")
public class TransferOrderController {

    private final TransferOrderService transferOrderService;

    public TransferOrderController(TransferOrderService transferOrderService) {
        this.transferOrderService = transferOrderService;
    }

    @GetMapping
    public ApiResponse<PageResult<TransferOrderEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status) {
        if (pageSize > 100) pageSize = 100;
        Page<TransferOrderEntity> result = transferOrderService.list(status, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<TransferOrderEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(transferOrderService.getById(id));
    }

    @PostMapping
    public ApiResponse<TransferOrderEntity> create(@RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        TransferOrderEntity order = new TransferOrderEntity();
        order.setTransferType(req.transferType());
        order.setRemark(req.remark());
        return ApiResponse.ok(transferOrderService.create(order, req.items(), userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<TransferOrderEntity> update(@PathVariable Long id, @RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        TransferOrderEntity order = new TransferOrderEntity();
        order.setTransferType(req.transferType());
        order.setRemark(req.remark());
        return ApiResponse.ok(transferOrderService.update(id, order, req.items(), userId));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Void> submit(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        transferOrderService.submit(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/audit")
    public ApiResponse<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body, @AuthenticationPrincipal Long userId) {
        transferOrderService.audit(id, body.get("status"), body.get("auditRemark"), userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/execute")
    public ApiResponse<Void> execute(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        transferOrderService.execute(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        transferOrderService.cancel(id, userId);
        return ApiResponse.ok();
    }

    record CreateRequest(String transferType, String remark, List<TransferOrderService.TransferOrderItemDTO> items) {}
}