package com.aicoding.warehouse.warehouselocation.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.warehouselocation.domain.WarehouseLocationService;
import com.aicoding.warehouse.warehouselocation.infra.WarehouseLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class WarehouseLocationController {

    private final WarehouseLocationService warehouseLocationService;

    public WarehouseLocationController(WarehouseLocationService warehouseLocationService) {
        this.warehouseLocationService = warehouseLocationService;
    }

    @GetMapping
    public ApiResponse<PageResult<WarehouseLocation>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String keyword) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<WarehouseLocation> result = warehouseLocationService.findAll(warehouseId, areaId, keyword, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseLocation> detail(@PathVariable Long id) {
        return ApiResponse.ok(warehouseLocationService.findById(id));
    }

    @PostMapping
    public ApiResponse<WarehouseLocation> create(@RequestBody LocationRequest request) {
        return ApiResponse.ok(warehouseLocationService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<WarehouseLocation> update(@PathVariable Long id, @RequestBody LocationRequest request) {
        return ApiResponse.ok(warehouseLocationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        warehouseLocationService.delete(id);
        return ApiResponse.ok();
    }
}
