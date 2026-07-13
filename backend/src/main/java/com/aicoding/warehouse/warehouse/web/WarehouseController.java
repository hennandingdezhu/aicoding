package com.aicoding.warehouse.warehouse.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.warehouse.domain.WarehouseService;
import com.aicoding.warehouse.warehouse.infra.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ApiResponse<PageResult<Warehouse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Warehouse> result = warehouseService.findAll(keyword, status, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Warehouse> detail(@PathVariable Long id) {
        return ApiResponse.ok(warehouseService.findById(id));
    }

    @PostMapping
    public ApiResponse<Warehouse> create(@RequestBody WarehouseRequest request,
                                          @AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(warehouseService.create(request, userId));
    }

    @PutMapping("/{id}")
    public ApiResponse<Warehouse> update(@PathVariable Long id,
                                          @RequestBody WarehouseRequest request,
                                          @AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(warehouseService.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return ApiResponse.ok();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        warehouseService.toggleStatus(id, body.get("status"));
        return ApiResponse.ok();
    }
}
