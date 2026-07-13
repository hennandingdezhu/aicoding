package com.aicoding.warehouse.warehousearea;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehouse.infra.Warehouse;
import com.aicoding.warehouse.warehouse.infra.WarehouseRepository;
import com.aicoding.warehouse.warehousearea.domain.WarehouseAreaService;
import com.aicoding.warehouse.warehousearea.infra.WarehouseArea;
import com.aicoding.warehouse.warehousearea.infra.WarehouseAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WarehouseAreaServiceTest {

    @Autowired
    private WarehouseAreaService warehouseAreaService;

    @Autowired
    private WarehouseAreaRepository warehouseAreaRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    private Long warehouseId;

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
    }

    @Test
    void shouldCreateWarehouseArea() {
        var request = new com.aicoding.warehouse.warehousearea.web.WarehouseAreaRequest(
                warehouseId, "A002", "B区");

        WarehouseArea created = warehouseAreaService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getAreaCode()).isEqualTo("A002");
        assertThat(created.getAreaName()).isEqualTo("B区");
        assertThat(created.getWarehouseId()).isEqualTo(warehouseId);
        assertThat(created.getStatus()).isEqualTo(1);
    }

    @Test
    void shouldRejectDuplicateAreaCodeInSameWarehouse() {
        var request = new com.aicoding.warehouse.warehousearea.web.WarehouseAreaRequest(
                warehouseId, "A001", "重复区");

        assertThatThrownBy(() -> warehouseAreaService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("库区编码已存在");
    }

    @Test
    void shouldFindById() {
        Long id = warehouseAreaRepository.findAll().get(0).getId();
        WarehouseArea found = warehouseAreaService.findById(id);

        assertThat(found.getAreaCode()).isEqualTo("A001");
        assertThat(found.getAreaName()).isEqualTo("A区");
    }

    @Test
    void shouldThrowWhenAreaNotFound() {
        assertThatThrownBy(() -> warehouseAreaService.findById(9999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("库区不存在");
    }

    @Test
    void shouldFindAllWithPagination() {
        Page<WarehouseArea> page = warehouseAreaService.findAll(warehouseId, "A区", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateWarehouseArea() {
        Long id = warehouseAreaRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.warehousearea.web.WarehouseAreaRequest(
                warehouseId, "A001", "A区-已更新");

        WarehouseArea updated = warehouseAreaService.update(id, request);

        assertThat(updated.getAreaName()).isEqualTo("A区-已更新");
    }

    @Test
    void shouldSoftDeleteArea() {
        Long id = warehouseAreaRepository.findAll().get(0).getId();
        warehouseAreaService.delete(id);

        WarehouseArea deleted = warehouseAreaRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }
}
