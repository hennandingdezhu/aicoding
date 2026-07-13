package com.aicoding.warehouse.warehousearea.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehousearea.domain.WarehouseAreaService;
import com.aicoding.warehouse.warehousearea.web.WarehouseAreaRequest;
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
public class WarehouseAreaServiceImpl implements WarehouseAreaService {

    private final WarehouseAreaRepository warehouseAreaRepository;

    public WarehouseAreaServiceImpl(WarehouseAreaRepository warehouseAreaRepository) {
        this.warehouseAreaRepository = warehouseAreaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseArea> findAll(Long warehouseId, String keyword, Pageable pageable) {
        Specification<WarehouseArea> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouseId"), warehouseId));
            }
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("areaCode"), like),
                        cb.like(root.get("areaName"), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return warehouseAreaRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseArea findById(Long id) {
        return warehouseAreaRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "库区不存在"));
    }

    @Override
    public WarehouseArea create(WarehouseAreaRequest request) {
        warehouseAreaRepository.findByWarehouseIdAndCode(request.warehouseId(), request.areaCode())
                .ifPresent(a -> { throw new BusinessException(409, "库区编码已存在"); });
        WarehouseArea area = new WarehouseArea();
        area.setWarehouseId(request.warehouseId());
        area.setAreaCode(request.areaCode());
        area.setAreaName(request.areaName());
        return warehouseAreaRepository.save(area);
    }

    @Override
    public WarehouseArea update(Long id, WarehouseAreaRequest request) {
        WarehouseArea area = findById(id);
        warehouseAreaRepository.findByWarehouseIdAndCode(request.warehouseId(), request.areaCode())
                .ifPresent(a -> { if (!a.getId().equals(id)) throw new BusinessException(409, "库区编码已存在"); });
        area.setWarehouseId(request.warehouseId());
        area.setAreaCode(request.areaCode());
        area.setAreaName(request.areaName());
        return warehouseAreaRepository.save(area);
    }

    @Override
    public void delete(Long id) {
        WarehouseArea area = findById(id);
        area.setDeleted(1);
        warehouseAreaRepository.save(area);
    }
}
