package com.aicoding.warehouse.report.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.stock.infra.StockJpaEntity;
import com.aicoding.warehouse.stock.infra.StockJpaRepository;
import com.aicoding.warehouse.stock.infra.StockLogJpaEntity;
import com.aicoding.warehouse.stock.infra.StockLogJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final StockJpaRepository stockJpaRepository;
    private final StockLogJpaRepository stockLogJpaRepository;
    @PersistenceContext
    private EntityManager em;

    public ReportController(StockJpaRepository stockJpaRepository, StockLogJpaRepository stockLogJpaRepository) {
        this.stockJpaRepository = stockJpaRepository;
        this.stockLogJpaRepository = stockLogJpaRepository;
    }

    @GetMapping("/stocks")
    public ApiResponse<List<StockJpaEntity>> stocks(@RequestParam(required = false) Long warehouseId,
                                                      @RequestParam(required = false) Long productId) {
        return ApiResponse.ok(stockJpaRepository.findWithFilters(productId, warehouseId, null));
    }

    @GetMapping("/inbound")
    public ApiResponse<List<Map<String, Object>>> inbound(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long warehouseId) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_no, o.status, o.inbound_type, o.warehouse_id, o.created_at, i.product_id, i.quantity " +
                "FROM inbound_order o JOIN inbound_order_item i ON o.id = i.inbound_order_id WHERE o.deleted = 0");
        if (warehouseId != null) sql.append(" AND o.warehouse_id = :warehouseId");
        if (startDate != null) sql.append(" AND o.created_at >= :startDate");
        if (endDate != null) sql.append(" AND o.created_at <= :endDate");
        sql.append(" ORDER BY o.created_at DESC");
        var query = em.createNativeQuery(sql.toString());
        if (warehouseId != null) query.setParameter("warehouseId", warehouseId);
        if (startDate != null) query.setParameter("startDate", LocalDate.parse(startDate).atStartOfDay());
        if (endDate != null) query.setParameter("endDate", LocalDate.parse(endDate).plusDays(1).atStartOfDay());
        return ApiResponse.ok(toMaps(query.getResultList()));
    }

    @GetMapping("/outbound")
    public ApiResponse<List<Map<String, Object>>> outbound(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long warehouseId) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_no, o.status, o.outbound_type, o.warehouse_id, o.created_at, i.product_id, i.quantity " +
                "FROM outbound_order o JOIN outbound_order_item i ON o.id = i.outbound_order_id WHERE o.deleted = 0");
        if (warehouseId != null) sql.append(" AND o.warehouse_id = :warehouseId");
        if (startDate != null) sql.append(" AND o.created_at >= :startDate");
        if (endDate != null) sql.append(" AND o.created_at <= :endDate");
        sql.append(" ORDER BY o.created_at DESC");
        var query = em.createNativeQuery(sql.toString());
        if (warehouseId != null) query.setParameter("warehouseId", warehouseId);
        if (startDate != null) query.setParameter("startDate", LocalDate.parse(startDate).atStartOfDay());
        if (endDate != null) query.setParameter("endDate", LocalDate.parse(endDate).plusDays(1).atStartOfDay());
        return ApiResponse.ok(toMaps(query.getResultList()));
    }

    @GetMapping("/product-stock-flow")
    public ApiResponse<List<StockLogJpaEntity>> productStockFlow(
            @RequestParam Long productId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        var spec = (org.springframework.data.jpa.domain.Specification<StockLogJpaEntity>) (root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.equal(root.get("productId"), productId));
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("operatedAt"), LocalDate.parse(startDate).atStartOfDay()));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("operatedAt"), LocalDate.parse(endDate).plusDays(1).atStartOfDay()));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ApiResponse.ok(stockLogJpaRepository.findAll(spec));
    }

    @GetMapping("/inventory-differences")
    public ApiResponse<List<Map<String, Object>>> inventoryDifferences(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.order_no, c.status, c.warehouse_id, c.created_at, i.product_id, i.book_quantity, i.actual_quantity, i.difference_quantity " +
                "FROM inventory_check c JOIN inventory_check_item i ON c.id = i.inventory_check_id WHERE c.deleted = 0");
        if (warehouseId != null) sql.append(" AND c.warehouse_id = :warehouseId");
        if (startDate != null) sql.append(" AND c.created_at >= :startDate");
        if (endDate != null) sql.append(" AND c.created_at <= :endDate");
        sql.append(" ORDER BY c.created_at DESC");
        var query = em.createNativeQuery(sql.toString());
        if (warehouseId != null) query.setParameter("warehouseId", warehouseId);
        if (startDate != null) query.setParameter("startDate", LocalDate.parse(startDate).atStartOfDay());
        if (endDate != null) query.setParameter("endDate", LocalDate.parse(endDate).plusDays(1).atStartOfDay());
        return ApiResponse.ok(toMaps(query.getResultList()));
    }

    @GetMapping("/transfers")
    public ApiResponse<List<Map<String, Object>>> transfers(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT t.order_no, t.status, t.transfer_type, t.created_at, i.product_id, i.from_warehouse_id, i.to_warehouse_id, i.quantity " +
                "FROM transfer_order t JOIN transfer_order_item i ON t.id = i.transfer_order_id WHERE t.deleted = 0");
        if (warehouseId != null) sql.append(" AND (i.from_warehouse_id = :warehouseId OR i.to_warehouseId = :warehouseId)");
        if (startDate != null) sql.append(" AND t.created_at >= :startDate");
        if (endDate != null) sql.append(" AND t.created_at <= :endDate");
        sql.append(" ORDER BY t.created_at DESC");
        var query = em.createNativeQuery(sql.toString());
        if (warehouseId != null) query.setParameter("warehouseId", warehouseId);
        if (startDate != null) query.setParameter("startDate", LocalDate.parse(startDate).atStartOfDay());
        if (endDate != null) query.setParameter("endDate", LocalDate.parse(endDate).plusDays(1).atStartOfDay());
        return ApiResponse.ok(toMaps(query.getResultList()));
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> toMaps(List<?> rows) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object row : rows) {
            Object[] arr = (Object[]) row;
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < arr.length; i++) {
                map.put("col" + i, arr[i]);
            }
            result.add(map);
        }
        return result;
    }
}