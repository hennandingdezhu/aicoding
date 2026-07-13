package com.aicoding.warehouse.warehouselocation.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.warehouselocation.domain.WarehouseLocationService;
import com.aicoding.warehouse.warehouselocation.web.LocationRequest;
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
public class WarehouseLocationServiceImpl implements WarehouseLocationService {

    private final WarehouseLocationRepository warehouseLocationRepository;

    public WarehouseLocationServiceImpl(WarehouseLocationRepository warehouseLocationRepository) {
        this.warehouseLocationRepository = warehouseLocationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseLocation> findAll(Long warehouseId, Long areaId, String keyword, Pageable pageable) {
        Specification<WarehouseLocation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouseId"), warehouseId));
            }
            if (areaId != null) {
                predicates.add(cb.equal(root.get("areaId"), areaId));
            }
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("locationCode"), like),
                        cb.like(root.get("locationName"), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return warehouseLocationRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseLocation findById(Long id) {
        return warehouseLocationRepository.findByIdActive(id)
                .orElseThrow(() -> new BusinessException(404, "库位不存在"));
    }

    @Override
    public WarehouseLocation create(LocationRequest request) {
        warehouseLocationRepository.findByCode(request.locationCode())
                .ifPresent(l -> { throw new BusinessException(409, "库位编码已存在"); });
        WarehouseLocation location = new WarehouseLocation();
        location.setWarehouseId(request.warehouseId());
        location.setAreaId(request.areaId());
        location.setLocationCode(request.locationCode());
        location.setLocationName(request.locationName());
        location.setCapacity(request.capacity());
        return warehouseLocationRepository.save(location);
    }

    @Override
    public WarehouseLocation update(Long id, LocationRequest request) {
        WarehouseLocation location = findById(id);
        warehouseLocationRepository.findByCode(request.locationCode())
                .ifPresent(l -> { if (!l.getId().equals(id)) throw new BusinessException(409, "库位编码已存在"); });
        location.setWarehouseId(request.warehouseId());
        location.setAreaId(request.areaId());
        location.setLocationCode(request.locationCode());
        location.setLocationName(request.locationName());
        location.setCapacity(request.capacity());
        return warehouseLocationRepository.save(location);
    }

    @Override
    public void delete(Long id) {
        WarehouseLocation location = findById(id);
        location.setDeleted(1);
        warehouseLocationRepository.save(location);
    }
}
