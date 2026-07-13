package com.aicoding.warehouse.supplier.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.supplier.domain.SupplierService;
import com.aicoding.warehouse.supplier.web.SupplierRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> findAll(String keyword, Integer status, Pageable pageable) {
        Specification<Supplier> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("supplierCode"), like),
                        cb.like(root.get("supplierName"), like),
                        cb.like(root.get("contactPerson"), like)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return supplierRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findById(Long id) {
        return supplierRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "供应商不存在"));
    }

    @Override
    public Supplier create(SupplierRequest request) {
        supplierRepository.findByCode(request.supplierCode())
                .ifPresent(s -> { throw new BusinessException(409, "供应商编码已存在"); });
        Supplier supplier = new Supplier();
        apply(request, supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Long id, SupplierRequest request) {
        Supplier supplier = findById(id);
        supplierRepository.findByCode(request.supplierCode())
                .ifPresent(s -> { if (!s.getId().equals(id)) throw new BusinessException(409, "供应商编码已存在"); });
        apply(request, supplier);
        return supplierRepository.save(supplier);
    }

    private void apply(SupplierRequest request, Supplier supplier) {
        supplier.setSupplierCode(request.supplierCode());
        supplier.setSupplierName(request.supplierName());
        supplier.setContactPerson(request.contactPerson());
        supplier.setContactPhone(request.contactPhone());
        supplier.setAddress(request.address());
        supplier.setRemark(request.remark());
    }

    @Override
    public void delete(Long id) {
        Supplier supplier = findById(id);
        supplier.setDeleted(1);
        supplierRepository.save(supplier);
    }
}
