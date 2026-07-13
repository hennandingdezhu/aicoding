package com.aicoding.warehouse.productcategory.domain;

import com.aicoding.warehouse.productcategory.infra.ProductCategory;
import com.aicoding.warehouse.productcategory.web.CategoryRequest;
import com.aicoding.warehouse.productcategory.web.CategoryTreeResponse;

import java.util.List;

public interface ProductCategoryService {
    List<CategoryTreeResponse> getTree();
    ProductCategory findById(Long id);
    ProductCategory create(CategoryRequest request);
    ProductCategory update(Long id, CategoryRequest request);
    void delete(Long id);
}
