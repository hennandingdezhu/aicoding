package com.aicoding.warehouse.customer;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.customer.domain.CustomerService;
import com.aicoding.warehouse.customer.infra.Customer;
import com.aicoding.warehouse.customer.infra.CustomerRepository;
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
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer c = new Customer();
        c.setCustomerCode("CUST-001");
        c.setCustomerName("北京科技有限公司");
        c.setContactPerson("李四");
        c.setContactPhone("13900000001");
        c.setAddress("北京市海淀区");
        c.setRemark("VIP客户");
        c.setStatus(1);
        customerRepository.save(c);
    }

    @Test
    void shouldCreateCustomer() {
        var request = new com.aicoding.warehouse.customer.web.CustomerRequest(
                "CUST-002", "上海互联网有限公司", "王五", "13900000002", "上海市徐汇区", "普通客户");

        Customer created = customerService.create(request);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getCustomerCode()).isEqualTo("CUST-002");
        assertThat(created.getCustomerName()).isEqualTo("上海互联网有限公司");
        assertThat(created.getStatus()).isEqualTo(1);
    }

    @Test
    void shouldRejectDuplicateCustomerCode() {
        var request = new com.aicoding.warehouse.customer.web.CustomerRequest(
                "CUST-001", "重复", "赵六", "13900000003", "地址", "备注");

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("客户编码已存在");
    }

    @Test
    void shouldFindById() {
        Long id = customerRepository.findAll().get(0).getId();
        Customer found = customerService.findById(id);
        assertThat(found.getCustomerCode()).isEqualTo("CUST-001");
    }

    @Test
    void shouldFindAllPaginated() {
        Page<Customer> page = customerService.findAll("北京", null, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateCustomer() {
        Long id = customerRepository.findAll().get(0).getId();
        var request = new com.aicoding.warehouse.customer.web.CustomerRequest(
                "CUST-001", "北京科技有限公司-已更新", "李四", "13900000001", "北京市朝阳区", "更新备注");

        Customer updated = customerService.update(id, request);
        assertThat(updated.getCustomerName()).isEqualTo("北京科技有限公司-已更新");
    }

    @Test
    void shouldSoftDeleteCustomer() {
        Long id = customerRepository.findAll().get(0).getId();
        customerService.delete(id);
        Customer deleted = customerRepository.findById(id).orElseThrow();
        assertThat(deleted.getDeleted()).isEqualTo(1);
    }
}
