package com.aicoding.warehouse.warehouselocation;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehouse.infra.Warehouse;
import com.aicoding.warehouse.warehouse.infra.WarehouseRepository;
import com.aicoding.warehouse.warehousearea.infra.WarehouseArea;
import com.aicoding.warehouse.warehousearea.infra.WarehouseAreaRepository;
import com.aicoding.warehouse.warehouselocation.domain.WarehouseLocationService;
import com.aicoding.warehouse.warehouselocation.infra.WarehouseLocation;
import com.aicoding.warehouse.warehouselocation.infra.WarehouseLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WarehouseLocationServiceTest {

    @Autowired
    private WarehouseLocationService warehouseLocationService;
    @Autowired
    private WarehouseLocationRepository warehouseLocationRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private WarehouseAreaRepository warehouseAreaRepository;

    private Long warehouseId;
    private Long areaId;

    @BeforeEach
    void setUp() {
        Warehouse w = new Warehouse();
        w.setWarehouseCode("WH001");
        w.setWarehouseName("主仓库");
        w.setStatus(1);
        warehouseRepository.save(w);
        warehouseId = w.getId();

        WarehouseArea area = new WarehouseArea();
        area.setWarehouseId(warehouseId);
        area.setAreaCode("A001");
        area.setAreaName("A区");
        area.setStatus(1);
        warehouseAreaRepository.save(area);
        areaId = area.getId();

        WarehouseLocation loc = new WarehouseLocation();
        loc.setWarehouseId(warehouseId);
        loc.setAreaId(areaId);
        loc.setLocationCode("LOC-001");
        loc.setLocationName("货架1号");
        loc.setCapacity(new BigDecimal("100.0000"));
        loc.setStatus(1);
        warehouseLocationRepository.save(loc);
    }

    @Test
    void shouldCreateWarehouseLocation() {
        var request = new com.aicoding.warehouse.warehouselocation.web.LocationRequest(
                warehouseId, areaId, "LOC-002", "货架2号", new BigDecimal("200.0000"));

        WarehouseLocation created = warehouseLocationService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getLocationCode()).isEqualTo("LOC-002");
        assertThat(created.getLocationName()).isEqualTo("货架2号");
        assertThat(created.getStatus()).isEqualTo(1);
        assertThat(created.getDeleted()).isEqualTo(0);
    }

    @Test
    void shouldRejectDuplicateLocationCode() {
        var request = new com.aicoding.warehouse.warehouselocation.web.LocationRequest(
                warehouseId, areaId, "LOC-001", "重复库位", new BigDecimal("100.0000"));

        assertThatThrownBy(() -> warehouseLocationService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("库位编码已存在");
    }

    @Test
    void shouldFindById() {
        Long id = warehouseLocationRepository.findAll().get(0).getId();
        WarehouseLocation found = warehouseLocationService.findById(id);
        assertThat(found.getLocationCode()).isEqualTo("LOC-001");
        assertThat(found.getLocationName()).isEqualTo("货架1号");
    }

    @Test
    void shouldThrowWhenLocationNotFound() {
        assertThatThrownBy(() -> warehouseLocationService.findById(9999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("库位不存在");
    }

    @Test
    void shouldFindAllWithPagination() {
        Page<WarehouseLocation> page = warehouseLocationService.findAll(warehouseId, areaId, "货架", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateWarehouseLocation() {
        Long id = warehouseLocationRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.warehouselocation.web.LocationRequest(
                warehouseId, areaId, "LOC-001", "货架1号-已更新", new BigDecimal("300.0000"));

        WarehouseLocation updated = warehouseLocationService.update(id, request);
        assertThat(updated.getLocationName()).isEqualTo("货架1号-已更新");
        assertThat(updated.getCapacity()).isEqualByComparingTo("300.0000");
    }

    @Test
    void shouldSoftDeleteLocation() {
        Long id = warehouseLocationRepository.findAll().get(0).getId();
        warehouseLocationService.delete(id);

        WarehouseLocation deleted = warehouseLocationRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }
}
