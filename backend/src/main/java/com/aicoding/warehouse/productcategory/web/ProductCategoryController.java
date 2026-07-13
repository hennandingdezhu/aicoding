package com.aicoding.warehouse.productcategory.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.productcategory.domain.ProductCategoryService;
import com.aicoding.warehouse.productcategory.infra.ProductCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/tree")
    public ApiResponse<List<CategoryTreeResponse>> tree() {
        return ApiResponse.ok(productCategoryService.getTree());
    }

    @PostMapping
    public ApiResponse<ProductCategory> create(@RequestBody CategoryRequest request) {
        return ApiResponse.ok(productCategoryService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductCategory> update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return ApiResponse.ok(productCategoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productCategoryService.delete(id);
        return ApiResponse.ok();
    }
}
