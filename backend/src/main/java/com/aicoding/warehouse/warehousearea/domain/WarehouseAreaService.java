package com.aicoding.warehouse.warehousearea.domain;

import com.aicoding.warehouse.warehousearea.infra.WarehouseArea;
import com.aicoding.warehouse.warehousearea.web.WarehouseAreaRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseAreaService {
    Page<WarehouseArea> findAll(Long warehouseId, String keyword, Pageable pageable);
    WarehouseArea findById(Long id);
    WarehouseArea create(WarehouseAreaRequest request);
    WarehouseArea update(Long id, WarehouseAreaRequest request);
    void delete(Long id);
}
