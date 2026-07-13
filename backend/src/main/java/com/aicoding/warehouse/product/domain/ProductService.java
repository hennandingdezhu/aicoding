package com.aicoding.warehouse.product.domain;

import com.aicoding.warehouse.product.infra.Product;
import com.aicoding.warehouse.product.web.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> findAll(String keyword, Long categoryId, Integer status, Pageable pageable);
    Product findById(Long id);
    Product create(ProductRequest request);
    Product update(Long id, ProductRequest request);
    void delete(Long id);
    void toggleStatus(Long id, Integer status);
}
