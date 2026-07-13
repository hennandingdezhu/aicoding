package com.aicoding.warehouse.warehouse.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehouse.domain.WarehouseService;
import com.aicoding.warehouse.warehouse.web.WarehouseRequest;
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
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Warehouse> findAll(String keyword, Integer status, Pageable pageable) {
        Specification<Warehouse> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("warehouseCode"), like),
                        cb.like(root.get("warehouseName"), like),
                        cb.like(root.get("managerName"), like)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return warehouseRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse findById(Long id) {
        return warehouseRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "仓库不存在"));
    }

    @Override
    public Warehouse create(WarehouseRequest request, Long userId) {
        warehouseRepository.findByCode(request.warehouseCode()).ifPresent(w -> {
            throw new BusinessException(409, "仓库编码已存在");
        });
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode(request.warehouseCode());
        warehouse.setWarehouseName(request.warehouseName());
        warehouse.setAddress(request.address());
        warehouse.setManagerName(request.managerName());
        warehouse.setManagerPhone(request.managerPhone());
        warehouse.setCreatedBy(userId);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse update(Long id, WarehouseRequest request, Long userId) {
        Warehouse warehouse = findById(id);
        warehouseRepository.findByCode(request.warehouseCode()).ifPresent(w -> {
            if (!w.getId().equals(id)) {
                throw new BusinessException(409, "仓库编码已存在");
            }
        });
        warehouse.setWarehouseCode(request.warehouseCode());
        warehouse.setWarehouseName(request.warehouseName());
        warehouse.setAddress(request.address());
        warehouse.setManagerName(request.managerName());
        warehouse.setManagerPhone(request.managerPhone());
        warehouse.setUpdatedBy(userId);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public void delete(Long id) {
        Warehouse warehouse = findById(id);
        warehouse.setDeleted(1);
        warehouseRepository.save(warehouse);
    }

    @Override
    public void toggleStatus(Long id, Integer status) {
        Warehouse warehouse = findById(id);
        warehouse.setStatus(status);
        warehouseRepository.save(warehouse);
    }
}
