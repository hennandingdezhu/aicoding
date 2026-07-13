package com.aicoding.warehouse.product.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.product.domain.ProductService;
import com.aicoding.warehouse.product.infra.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<PageResult<Product>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> result = productService.findAll(keyword, categoryId, status, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> detail(@PathVariable Long id) {
        return ApiResponse.ok(productService.findById(id));
    }

    @PostMapping
    public ApiResponse<Product> create(@RequestBody ProductRequest request) {
        return ApiResponse.ok(productService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ApiResponse.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        productService.toggleStatus(id, body.get("status"));
        return ApiResponse.ok();
    }
}
