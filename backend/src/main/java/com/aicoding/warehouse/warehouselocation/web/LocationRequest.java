package com.aicoding.warehouse.warehouselocation.web;

import java.math.BigDecimal;

public record LocationRequest(Long warehouseId, Long areaId, String locationCode, String locationName, BigDecimal capacity) {}
