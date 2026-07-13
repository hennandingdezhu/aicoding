package com.aicoding.warehouse.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysUserWarehouseRepository extends JpaRepository<SysUserWarehouse, Long> {
    List<SysUserWarehouse> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByUserIdAndWarehouseIdIn(Long userId, List<Long> warehouseIds);
}
