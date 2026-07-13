package com.aicoding.warehouse.supplier.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.supplier.domain.SupplierService;
import com.aicoding.warehouse.supplier.infra.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ApiResponse<PageResult<Supplier>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Supplier> result = supplierService.findAll(keyword, status, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Supplier> detail(@PathVariable Long id) {
        return ApiResponse.ok(supplierService.findById(id));
    }

    @PostMapping
    public ApiResponse<Supplier> create(@RequestBody SupplierRequest request) {
        return ApiResponse.ok(supplierService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Supplier> update(@PathVariable Long id, @RequestBody SupplierRequest request) {
        return ApiResponse.ok(supplierService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ApiResponse.ok();
    }
}
