package com.aicoding.warehouse.stock.infra;

import com.aicoding.warehouse.stock.domain.Stock;
import com.aicoding.warehouse.stock.domain.StockRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockJpaRepository extends JpaRepository<StockJpaEntity, StockId>, StockRepository {

    @Override
    default Optional<Stock> findBySkuLocation(Long productId, Long warehouseId, Long areaId, Long locationId) {
        return findEntityBySkuLocation(productId, warehouseId, areaId, locationId)
                .map(this::toDomain);
    }

    @Override
    default Stock save(Stock stock) {
        StockJpaEntity entity = toEntity(stock);
        StockJpaEntity saved = saveAndFlush(entity);
        return toDomain(saved);
    }

    @Query("SELECT s FROM StockJpaEntity s WHERE s.productId = :productId AND s.warehouseId = :warehouseId AND s.areaId = :areaId AND s.locationId = :locationId")
    Optional<StockJpaEntity> findEntityBySkuLocation(@Param("productId") Long productId,
                                                      @Param("warehouseId") Long warehouseId,
                                                      @Param("areaId") Long areaId,
                                                      @Param("locationId") Long locationId);

    @Query("SELECT s FROM StockJpaEntity s WHERE (:productId IS NULL OR s.productId = :productId) AND (:warehouseId IS NULL OR s.warehouseId = :warehouseId) AND (:locationId IS NULL OR s.locationId = :locationId)")
    List<StockJpaEntity> findWithFilters(@Param("productId") Long productId,
                                          @Param("warehouseId") Long warehouseId,
                                          @Param("locationId") Long locationId);

    @Query("SELECT s FROM StockJpaEntity s WHERE (:warehouseId IS NULL OR s.warehouseId = :warehouseId) AND (s.availableQuantity <= s.warningMin OR s.totalQuantity >= s.warningMax)")
    List<StockJpaEntity> findWarnings(@Param("warehouseId") Long warehouseId);

    @Query("SELECT s FROM StockJpaEntity s WHERE s.productId = :productId AND s.warehouseId = :warehouseId AND s.areaId = :areaId AND s.locationId = :locationId")
    List<StockJpaEntity> findByProductWarehouseAreaLocation(@Param("productId") Long productId,
                                                             @Param("warehouseId") Long warehouseId,
                                                             @Param("areaId") Long areaId,
                                                             @Param("locationId") Long locationId);

    default Stock toDomain(StockJpaEntity entity) {
        Stock stock = new Stock(entity.getProductId(), entity.getWarehouseId(), entity.getAreaId(), entity.getLocationId());
        stock.setTotalQuantity(entity.getTotalQuantity() != null ? entity.getTotalQuantity() : BigDecimal.ZERO);
        stock.setAvailableQuantity(entity.getAvailableQuantity() != null ? entity.getAvailableQuantity() : BigDecimal.ZERO);
        stock.setLockedQuantity(entity.getLockedQuantity() != null ? entity.getLockedQuantity() : BigDecimal.ZERO);
        stock.setWarningMin(entity.getWarningMin());
        stock.setWarningMax(entity.getWarningMax());
        return stock;
    }

    default StockJpaEntity toEntity(Stock stock) {
        Optional<StockJpaEntity> existing = findEntityBySkuLocation(
                stock.getProductId(), stock.getWarehouseId(), stock.getAreaId(), stock.getLocationId());
        StockJpaEntity entity = existing.orElse(new StockJpaEntity());
        entity.setProductId(stock.getProductId());
        entity.setWarehouseId(stock.getWarehouseId());
        entity.setAreaId(stock.getAreaId());
        entity.setLocationId(stock.getLocationId());
        entity.setTotalQuantity(stock.getTotalQuantity());
        entity.setAvailableQuantity(stock.getAvailableQuantity());
        entity.setLockedQuantity(stock.getLockedQuantity());
        entity.setWarningMin(stock.getWarningMin());
        entity.setWarningMax(stock.getWarningMax());
        return entity;
    }
}