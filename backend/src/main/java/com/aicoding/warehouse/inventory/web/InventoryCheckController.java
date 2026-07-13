package com.aicoding.warehouse.inventory.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.inventory.domain.InventoryCheckService;
import com.aicoding.warehouse.inventory.infra.InventoryCheckEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory-checks")
public class InventoryCheckController {

    private final InventoryCheckService inventoryCheckService;

    public InventoryCheckController(InventoryCheckService inventoryCheckService) {
        this.inventoryCheckService = inventoryCheckService;
    }

    @GetMapping
    public ApiResponse<PageResult<InventoryCheckEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String status) {
        Page<InventoryCheckEntity> result = inventoryCheckService.list(warehouseId, status, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<InventoryCheckEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(inventoryCheckService.getById(id));
    }

    @PostMapping
    public ApiResponse<InventoryCheckEntity> create(@RequestBody CreateRequest req, @AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(inventoryCheckService.create(req.warehouseId(), req.areaId(), req.locationId(), req.remark(), userId));
    }

    @PutMapping("/{id}/items")
    public ApiResponse<Void> updateItems(@PathVariable Long id, @RequestBody UpdateItemsRequest req, @AuthenticationPrincipal Long userId) {
        inventoryCheckService.updateItems(id, req.items(), userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Void> submit(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inventoryCheckService.submit(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/audit")
    public ApiResponse<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body, @AuthenticationPrincipal Long userId) {
        inventoryCheckService.audit(id, body.get("status"), body.get("auditRemark"), userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/adjust")
    public ApiResponse<Void> adjust(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inventoryCheckService.adjust(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        inventoryCheckService.cancel(id, userId);
        return ApiResponse.ok();
    }

    record CreateRequest(Long warehouseId, Long areaId, Long locationId, String remark) {}
    record UpdateItemsRequest(List<InventoryCheckService.CheckItemUpdate> items) {}
}