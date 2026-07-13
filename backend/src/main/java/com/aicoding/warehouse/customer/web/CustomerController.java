package com.aicoding.warehouse.customer.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.customer.domain.CustomerService;
import com.aicoding.warehouse.customer.infra.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ApiResponse<PageResult<Customer>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Customer> result = customerService.findAll(keyword, status, pageable);
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Customer> detail(@PathVariable Long id) {
        return ApiResponse.ok(customerService.findById(id));
    }

    @PostMapping
    public ApiResponse<Customer> create(@RequestBody CustomerRequest request) {
        return ApiResponse.ok(customerService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Customer> update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        return ApiResponse.ok(customerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ApiResponse.ok();
    }
}
