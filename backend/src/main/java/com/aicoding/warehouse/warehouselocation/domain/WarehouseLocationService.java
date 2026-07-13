package com.aicoding.warehouse.warehouselocation.domain;

import com.aicoding.warehouse.warehouselocation.infra.WarehouseLocation;
import com.aicoding.warehouse.warehouselocation.web.LocationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseLocationService {
    Page<WarehouseLocation> findAll(Long warehouseId, Long areaId, String keyword, Pageable pageable);
    WarehouseLocation findById(Long id);
    WarehouseLocation create(LocationRequest request);
    WarehouseLocation update(Long id, LocationRequest request);
    void delete(Long id);
}
