package com.aicoding.warehouse.warehouse;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehouse.domain.WarehouseService;
import com.aicoding.warehouse.warehouse.infra.Warehouse;
import com.aicoding.warehouse.warehouse.infra.WarehouseRepository;
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
class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
        Warehouse w = new Warehouse();
        w.setWarehouseCode("WH001");
        w.setWarehouseName("主仓库");
        w.setAddress("北京市朝阳区");
        w.setManagerName("张三");
        w.setManagerPhone("13800000001");
        w.setStatus(1);
        warehouseRepository.save(w);
    }

    @Test
    void shouldCreateWarehouse() {
        var request = new com.aicoding.warehouse.warehouse.web.WarehouseRequest(
                "WH002", "副仓库", "上海市浦东新区", "李四", "13800000002");

        Warehouse created = warehouseService.create(request, 1L);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getWarehouseCode()).isEqualTo("WH002");
        assertThat(created.getWarehouseName()).isEqualTo("副仓库");
        assertThat(created.getCreatedBy()).isEqualTo(1L);
        assertThat(created.getStatus()).isEqualTo(1);
        assertThat(created.getDeleted()).isEqualTo(0);
    }

    @Test
    void shouldRejectDuplicateWarehouseCode() {
        var request = new com.aicoding.warehouse.warehouse.web.WarehouseRequest(
                "WH001", "副仓库", "上海市浦东新区", "李四", "13800000002");

        assertThatThrownBy(() -> warehouseService.create(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("仓库编码已存在");
    }

    @Test
    void shouldFindWarehouseById() {
        Warehouse found = warehouseService.findById(warehouseRepository.findAll().get(0).getId());

        assertThat(found.getWarehouseCode()).isEqualTo("WH001");
        assertThat(found.getWarehouseName()).isEqualTo("主仓库");
    }

    @Test
    void shouldThrowWhenWarehouseNotFound() {
        assertThatThrownBy(() -> warehouseService.findById(9999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("仓库不存在");
    }

    @Test
    void shouldFindAllWithPaginationAndFilters() {
        Page<Warehouse> page = warehouseService.findAll("主", null, PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getWarehouseName()).isEqualTo("主仓库");

        Page<Warehouse> emptyPage = warehouseService.findAll("不存在的", null, PageRequest.of(0, 10));
        assertThat(emptyPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    void shouldUpdateWarehouse() {
        Long id = warehouseRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.warehouse.web.WarehouseRequest(
                "WH001", "主仓库-已更新", "北京市海淀区", "王五", "13800000003");

        Warehouse updated = warehouseService.update(id, request, 2L);

        assertThat(updated.getWarehouseName()).isEqualTo("主仓库-已更新");
        assertThat(updated.getAddress()).isEqualTo("北京市海淀区");
        assertThat(updated.getManagerName()).isEqualTo("王五");
        assertThat(updated.getUpdatedBy()).isEqualTo(2L);
    }

    @Test
    void shouldSoftDeleteWarehouse() {
        Long id = warehouseRepository.findAll().get(0).getId();

        warehouseService.delete(id);

        Warehouse deleted = warehouseRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }

    @Test
    void shouldToggleWarehouseStatus() {
        Long id = warehouseRepository.findAll().get(0).getId();

        warehouseService.toggleStatus(id, 0);
        Warehouse disabled = warehouseRepository.findById(id).orElseThrow();
        assertThat(disabled.getStatus()).isEqualTo(0);

        warehouseService.toggleStatus(id, 1);
        Warehouse enabled = warehouseRepository.findById(id).orElseThrow();
        assertThat(enabled.getStatus()).isEqualTo(1);
    }
}
