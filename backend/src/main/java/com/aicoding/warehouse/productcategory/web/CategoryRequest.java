package com.aicoding.warehouse.productcategory.web;

public record CategoryRequest(Long parentId, String categoryCode, String categoryName, Integer sortOrder) {}
