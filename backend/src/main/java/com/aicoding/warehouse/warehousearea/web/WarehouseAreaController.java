package com.aicoding.warehouse.warehousearea.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.warehousearea.domain.WarehouseAreaService;
import com.aicoding.warehouse.warehousearea.infra.WarehouseArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-areas")
public class WarehouseAreaController {

    private final WarehouseAreaService warehouseAreaService;

    public WarehouseAreaController(WarehouseAreaService warehouseAreaService) {
        this.warehouseAreaService = warehouseAreaService;
    }

    @GetMapping
    public ApiResponse<PageResult<WarehouseArea>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String keyword) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<WarehouseArea> result = warehouseAreaService.findAll(warehouseId, keyword, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseArea> detail(@PathVariable Long id) {
        return ApiResponse.ok(warehouseAreaService.findById(id));
    }

    @PostMapping
    public ApiResponse<WarehouseArea> create(@RequestBody WarehouseAreaRequest request) {
        return ApiResponse.ok(warehouseAreaService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<WarehouseArea> update(@PathVariable Long id, @RequestBody WarehouseAreaRequest request) {
        return ApiResponse.ok(warehouseAreaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        warehouseAreaService.delete(id);
        return ApiResponse.ok();
    }
}
