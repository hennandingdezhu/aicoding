package com.aicoding.warehouse.supplier.domain;

import com.aicoding.warehouse.supplier.infra.Supplier;
import com.aicoding.warehouse.supplier.web.SupplierRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    Page<Supplier> findAll(String keyword, Integer status, Pageable pageable);
    Supplier findById(Long id);
    Supplier create(SupplierRequest request);
    Supplier update(Long id, SupplierRequest request);
    void delete(Long id);
}
