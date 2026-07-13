package com.aicoding.warehouse.inventory.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.inventory.domain.InventoryCheckService;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockService;
import com.aicoding.warehouse.stock.infra.StockJpaEntity;
import com.aicoding.warehouse.stock.infra.StockJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class InventoryCheckServiceImpl implements InventoryCheckService {

    private final InventoryCheckRepository inventoryCheckRepository;
    private final StockJpaRepository stockJpaRepository;
    private final StockService stockService;

    public InventoryCheckServiceImpl(InventoryCheckRepository inventoryCheckRepository,
                                      StockJpaRepository stockJpaRepository,
                                      StockService stockService) {
        this.inventoryCheckRepository = inventoryCheckRepository;
        this.stockJpaRepository = stockJpaRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryCheckEntity> list(Long warehouseId, String status, Pageable pageable) {
        return inventoryCheckRepository.findWithFilters(warehouseId, status, pageable);
    }

    @Override
    public InventoryCheckEntity getById(Long id) {
        return inventoryCheckRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "盘点单不存在"));
    }

    @Override
    public InventoryCheckEntity create(Long warehouseId, Long areaId, Long locationId, String remark, Long userId) {
        InventoryCheckEntity check = new InventoryCheckEntity();
        check.setWarehouseId(warehouseId);
        check.setAreaId(areaId);
        check.setLocationId(locationId);
        check.setStatus("DRAFT");
        check.setRemark(remark);
        check.setCreatedBy(userId);
        check.setOrderNo(generateOrderNo());

        List<StockJpaEntity> stocks = stockJpaRepository.findWithFilters(null, warehouseId, locationId);
        for (StockJpaEntity stock : stocks) {
            if (areaId != null && !areaId.equals(stock.getAreaId())) continue;
            InventoryCheckItemEntity item = new InventoryCheckItemEntity();
            item.setProductId(stock.getProductId());
            item.setWarehouseId(stock.getWarehouseId());
            item.setAreaId(stock.getAreaId());
            item.setLocationId(stock.getLocationId());
            item.setBookQuantity(stock.getTotalQuantity() != null ? stock.getTotalQuantity() : BigDecimal.ZERO);
            check.addItem(item);
        }
        return inventoryCheckRepository.save(check);
    }

    @Override
    public void updateItems(Long id, List<CheckItemUpdate> updates, Long userId) {
        InventoryCheckEntity check = getById(id);
        if (!"DRAFT".equals(check.getStatus())) {
            throw new BusinessException("只有草稿状态可录入实盘数量");
        }
        for (CheckItemUpdate update : updates) {
            check.getItems().stream()
                    .filter(i -> i.getId().equals(update.itemId()))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setActualQuantity(update.actualQuantity());
                        item.setRemark(update.remark());
                        item.setDifferenceQuantity(
                                (update.actualQuantity() != null ? update.actualQuantity() : BigDecimal.ZERO)
                                        .subtract(item.getBookQuantity()));
                    });
        }
        check.setUpdatedBy(userId);
        inventoryCheckRepository.save(check);
    }

    @Override
    public void submit(Long id, Long userId) {
        InventoryCheckEntity check = getById(id);
        if (!"DRAFT".equals(check.getStatus())) {
            throw new BusinessException("只有草稿状态可提交");
        }
        check.setStatus("PENDING");
        check.setSubmittedAt(LocalDateTime.now());
        check.setUpdatedBy(userId);
        inventoryCheckRepository.save(check);
    }

    @Override
    public void audit(Long id, String status, String auditRemark, Long userId) {
        InventoryCheckEntity check = getById(id);
        if (!"PENDING".equals(check.getStatus())) {
            throw new BusinessException("只有待审核状态可审核");
        }
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new BusinessException("审核状态无效");
        }
        check.setStatus(status);
        check.setAuditedBy(userId);
        check.setAuditedAt(LocalDateTime.now());
        check.setAuditRemark(auditRemark);
        check.setUpdatedBy(userId);
        inventoryCheckRepository.save(check);
    }

    @Override
    public void adjust(Long id, Long userId) {
        InventoryCheckEntity check = getById(id);
        if (!"APPROVED".equals(check.getStatus())) {
            throw new BusinessException("只有已审核状态可调整库存");
        }
        for (InventoryCheckItemEntity item : check.getItems()) {
            BigDecimal diff = item.getDifferenceQuantity() != null ? item.getDifferenceQuantity() : BigDecimal.ZERO;
            if (diff.compareTo(BigDecimal.ZERO) == 0) continue;
            StockCommand cmd = new StockCmd(item.getProductId(), item.getWarehouseId(),
                    item.getAreaId(), item.getLocationId(), diff.abs());
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                // 盘盈：直接增加库存
                stockService.confirmInbound(cmd, check.getOrderNo(), userId);
            } else {
                // 盘亏：直接减少可用库存（无需预锁定）
                stockService.adjustOutbound(cmd, check.getOrderNo(), userId);
            }
        }
        check.setStatus("ADJUSTED");
        check.setAdjustedAt(LocalDateTime.now());
        check.setUpdatedBy(userId);
        inventoryCheckRepository.save(check);
    }

    @Override
    public void cancel(Long id, Long userId) {
        InventoryCheckEntity check = getById(id);
        if ("ADJUSTED".equals(check.getStatus()) || "CANCELLED".equals(check.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }
        check.setStatus("CANCELLED");
        check.setUpdatedBy(userId);
        inventoryCheckRepository.save(check);
    }

    private String generateOrderNo() {
        String prefix = "IC-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-%";
        int maxSeq = inventoryCheckRepository.findMaxSeq(prefix);
        return "IC-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%06d", maxSeq + 1);
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}
