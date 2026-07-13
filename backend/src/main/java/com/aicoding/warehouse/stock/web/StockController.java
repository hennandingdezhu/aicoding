package com.aicoding.warehouse.stock.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.stock.infra.StockJpaEntity;
import com.aicoding.warehouse.stock.infra.StockJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockJpaRepository stockJpaRepository;

    public StockController(StockJpaRepository stockJpaRepository) {
        this.stockJpaRepository = stockJpaRepository;
    }

    @GetMapping
    public ApiResponse<PageResult<StockJpaEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long locationId) {
        List<StockJpaEntity> all = stockJpaRepository.findWithFilters(productId, warehouseId, locationId);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        List<StockJpaEntity> pageContent = start < all.size() ? all.subList(start, end) : List.of();
        return ApiResponse.ok(new PageResult<>(pageContent, all.size(), page, pageSize));
    }

    @GetMapping("/detail")
    public ApiResponse<StockJpaEntity> getByKeys(@RequestParam Long productId, @RequestParam Long warehouseId,
                                                  @RequestParam Long areaId, @RequestParam Long locationId) {
        var entities = stockJpaRepository.findByProductWarehouseAreaLocation(productId, warehouseId, areaId, locationId);
        if (entities.isEmpty()) throw new BusinessException(404, "库存记录不存在");
        return ApiResponse.ok(entities.get(0));
    }

    @PutMapping("/warning")
    public ApiResponse<Void> setWarning(@RequestParam Long productId, @RequestParam Long warehouseId,
                                         @RequestParam Long areaId, @RequestParam Long locationId,
                                         @RequestBody Map<String, BigDecimal> body) {
        var entities = stockJpaRepository.findByProductWarehouseAreaLocation(productId, warehouseId, areaId, locationId);
        if (entities.isEmpty()) throw new BusinessException(404, "库存记录不存在");
        StockJpaEntity stock = entities.get(0);
        if (body.containsKey("warningMin")) stock.setWarningMin(body.get("warningMin"));
        if (body.containsKey("warningMax")) stock.setWarningMax(body.get("warningMax"));
        stockJpaRepository.save(stock);
        return ApiResponse.ok();
    }

    @GetMapping("/warnings")
    public ApiResponse<PageResult<StockJpaEntity>> warnings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long warehouseId) {
        List<StockJpaEntity> all = stockJpaRepository.findWarnings(warehouseId);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        List<StockJpaEntity> pageContent = start < all.size() ? all.subList(start, end) : List.of();
        return ApiResponse.ok(new PageResult<>(pageContent, all.size(), page, pageSize));
    }

    @GetMapping("/export")
    public ApiResponse<List<StockJpaEntity>> exportStocks(@RequestParam(required = false) Long warehouseId) {
        return ApiResponse.ok(stockJpaRepository.findWithFilters(null, warehouseId, null));
    }
}
