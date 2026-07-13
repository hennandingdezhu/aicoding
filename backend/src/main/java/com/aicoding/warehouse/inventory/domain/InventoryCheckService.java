package com.aicoding.warehouse.inventory.domain;

import com.aicoding.warehouse.inventory.infra.InventoryCheckEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryCheckService {

    Page<InventoryCheckEntity> list(Long warehouseId, String status, Pageable pageable);

    InventoryCheckEntity getById(Long id);

    InventoryCheckEntity create(Long warehouseId, Long areaId, Long locationId, String remark, Long userId);

    void updateItems(Long id, List<CheckItemUpdate> items, Long userId);

    void submit(Long id, Long userId);

    void audit(Long id, String status, String auditRemark, Long userId);

    void adjust(Long id, Long userId);

    void cancel(Long id, Long userId);

    record CheckItemUpdate(Long itemId, java.math.BigDecimal actualQuantity, String remark) {}
}