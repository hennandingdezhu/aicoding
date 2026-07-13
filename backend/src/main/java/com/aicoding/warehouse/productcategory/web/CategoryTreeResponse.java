package com.aicoding.warehouse.productcategory.web;

import java.util.List;

public record CategoryTreeResponse(Long id, Long parentId, String categoryCode, String categoryName, Integer sortOrder, Integer status, List<CategoryTreeResponse> children) {}
