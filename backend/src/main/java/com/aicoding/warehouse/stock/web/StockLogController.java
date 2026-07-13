package com.aicoding.warehouse.stock.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.stock.infra.StockLogJpaEntity;
import com.aicoding.warehouse.stock.infra.StockLogJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stock-logs")
public class StockLogController {

    private final StockLogJpaRepository stockLogJpaRepository;

    public StockLogController(StockLogJpaRepository stockLogJpaRepository) {
        this.stockLogJpaRepository = stockLogJpaRepository;
    }

    @GetMapping
    public ApiResponse<PageResult<StockLogJpaEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String changeType) {
        if (pageSize > 100) pageSize = 100;
        Specification<StockLogJpaEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (productId != null) predicates.add(cb.equal(root.get("productId"), productId));
            if (warehouseId != null) predicates.add(cb.equal(root.get("warehouseId"), warehouseId));
            if (changeType != null) predicates.add(cb.equal(root.get("changeType"), changeType));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<StockLogJpaEntity> result = stockLogJpaRepository.findAll(spec, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<StockLogJpaEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(stockLogJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "閹煎瓨鎸搁悺銊ッ规担瑙勫瘻濞戞挸绉撮悺銊╁捶?)));
    }
}