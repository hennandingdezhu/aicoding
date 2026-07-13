package com.aicoding.warehouse.product.web;

import java.math.BigDecimal;

public record ProductRequest(
        String productCode, String productName, Long categoryId,
        String specification, String unit, String brand,
        BigDecimal costPrice, BigDecimal salePrice, Long imageFileId, String remark) {}
