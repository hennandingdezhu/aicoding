package com.aicoding.warehouse.warehouse.domain;

import com.aicoding.warehouse.warehouse.infra.Warehouse;
import com.aicoding.warehouse.warehouse.web.WarehouseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    Page<Warehouse> findAll(String keyword, Integer status, Pageable pageable);
    Warehouse findById(Long id);
    Warehouse create(WarehouseRequest request, Long userId);
    Warehouse update(Long id, WarehouseRequest request, Long userId);
    void delete(Long id);
    void toggleStatus(Long id, Integer status);
}
