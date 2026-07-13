package com.aicoding.warehouse.supplier;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.supplier.domain.SupplierService;
import com.aicoding.warehouse.supplier.infra.Supplier;
import com.aicoding.warehouse.supplier.infra.SupplierRepository;
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
class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    void setUp() {
        Supplier s = new Supplier();
        s.setSupplierCode("SUP-001");
        s.setSupplierName("深圳电子有限公司");
        s.setContactPerson("张三");
        s.setContactPhone("13800000001");
        s.setAddress("深圳市宝安区");
        s.setRemark("主要供应商");
        s.setStatus(1);
        supplierRepository.save(s);
    }

    @Test
    void shouldCreateSupplier() {
        var request = new com.aicoding.warehouse.supplier.web.SupplierRequest(
                "SUP-002", "上海贸易有限公司", "李四", "13800000002", "上海市浦东新区", "备用供应商");

        Supplier created = supplierService.create(request);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getSupplierCode()).isEqualTo("SUP-002");
        assertThat(created.getSupplierName()).isEqualTo("上海贸易有限公司");
        assertThat(created.getStatus()).isEqualTo(1);
    }

    @Test
    void shouldRejectDuplicateSupplierCode() {
        var request = new com.aicoding.warehouse.supplier.web.SupplierRequest(
                "SUP-001", "重复", "王五", "13800000003", "地址", "备注");

        assertThatThrownBy(() -> supplierService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("供应商编码已存在");
    }

    @Test
    void shouldFindById() {
        Long id = supplierRepository.findAll().get(0).getId();
        Supplier found = supplierService.findById(id);
        assertThat(found.getSupplierCode()).isEqualTo("SUP-001");
    }

    @Test
    void shouldFindAllPaginated() {
        Page<Supplier> page = supplierService.findAll("深圳", null, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateSupplier() {
        Long id = supplierRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.supplier.web.SupplierRequest(
                "SUP-001", "深圳电子有限公司-已更新", "张三", "13800000001", "深圳市南山区", "更新备注");

        Supplier updated = supplierService.update(id, request);
        assertThat(updated.getSupplierName()).isEqualTo("深圳电子有限公司-已更新");
    }

    @Test
    void shouldSoftDeleteSupplier() {
        Long id = supplierRepository.findAll().get(0).getId();
        supplierService.delete(id);
        Supplier deleted = supplierRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }
}
