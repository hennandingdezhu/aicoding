package com.aicoding.warehouse.stock.infra;

import com.aicoding.warehouse.stock.domain.StockLog;
import com.aicoding.warehouse.stock.domain.StockLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLogJpaRepository extends JpaRepository<StockLogJpaEntity, Long>, JpaSpecificationExecutor<StockLogJpaEntity>, StockLogRepository {

    @Override
    default StockLog save(StockLog stockLog) {
        StockLogJpaEntity entity = new StockLogJpaEntity();
        entity.setLogNo(stockLog.getLogNo());
        entity.setProductId(stockLog.getProductId());
        entity.setWarehouseId(stockLog.getWarehouseId());
        entity.setAreaId(stockLog.getAreaId());
        entity.setLocationId(stockLog.getLocationId());
        entity.setChangeType(stockLog.getChangeType());
        entity.setBeforeQuantity(stockLog.getBeforeQuantity());
        entity.setChangeQuantity(stockLog.getChangeQuantity());
        entity.setAfterQuantity(stockLog.getAfterQuantity());
        entity.setBusinessNo(stockLog.getBusinessNo());
        entity.setOperatedBy(stockLog.getOperatedBy());
        entity.setOperatedAt(stockLog.getOperatedAt());
        StockLogJpaEntity saved = save(entity);
        stockLog.setId(saved.getId());
        return stockLog;
    }
}