package com.aicoding.warehouse.customer.domain;

import com.aicoding.warehouse.customer.infra.Customer;
import com.aicoding.warehouse.customer.web.CustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<Customer> findAll(String keyword, Integer status, Pageable pageable);
    Customer findById(Long id);
    Customer create(CustomerRequest request);
    Customer update(Long id, CustomerRequest request);
    void delete(Long id);
}
