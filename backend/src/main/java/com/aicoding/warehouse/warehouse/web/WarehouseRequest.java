package com.aicoding.warehouse.warehouse.web;

public record WarehouseRequest(
        String warehouseCode,
        String warehouseName,
        String address,
        String managerName,
        String managerPhone
) {}
