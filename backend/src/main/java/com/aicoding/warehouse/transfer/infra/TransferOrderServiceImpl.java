package com.aicoding.warehouse.transfer.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockService;
import com.aicoding.warehouse.transfer.domain.TransferOrderService;
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
public class TransferOrderServiceImpl implements TransferOrderService {

    private final TransferOrderRepository transferOrderRepository;
    private final StockService stockService;

    public TransferOrderServiceImpl(TransferOrderRepository transferOrderRepository, StockService stockService) {
        this.transferOrderRepository = transferOrderRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransferOrderEntity> list(String status, Pageable pageable) {
        return transferOrderRepository.findWithFilters(status, pageable);
    }

    @Override
    public TransferOrderEntity getById(Long id) {
        return transferOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "调拨单不存在"));
    }

    @Override
    public TransferOrderEntity create(TransferOrderEntity order, List<TransferOrderItemDTO> items, Long userId) {
        order.setStatus("DRAFT");
        order.setCreatedBy(userId);
        order.setOrderNo(generateOrderNo());
        for (TransferOrderItemDTO dto : items) {
            if (dto.fromWarehouseId().equals(dto.toWarehouseId())
                    && dto.fromAreaId().equals(dto.toAreaId())
                    && dto.fromLocationId().equals(dto.toLocationId())) {
                throw new BusinessException("调出和调入库位不能相同");
            }
            TransferOrderItemEntity item = new TransferOrderItemEntity();
            item.setProductId(dto.productId());
            item.setFromWarehouseId(dto.fromWarehouseId());
            item.setFromAreaId(dto.fromAreaId());
            item.setFromLocationId(dto.fromLocationId());
            item.setToWarehouseId(dto.toWarehouseId());
            item.setToAreaId(dto.toAreaId());
            item.setToLocationId(dto.toLocationId());
            item.setQuantity(dto.quantity());
            item.setRemark(dto.remark());
            order.addItem(item);
        }
        return transferOrderRepository.save(order);
    }

    @Override
    public TransferOrderEntity update(Long id, TransferOrderEntity order, List<TransferOrderItemDTO> items, Long userId) {
        TransferOrderEntity existing = getById(id);
        if (!"DRAFT".equals(existing.getStatus())) {
            throw new BusinessException("只有草稿状态可编辑");
        }
        existing.setTransferType(order.getTransferType());
        existing.setRemark(order.getRemark());
        existing.setUpdatedBy(userId);
        existing.getItems().clear();
        for (TransferOrderItemDTO dto : items) {
            if (dto.fromWarehouseId().equals(dto.toWarehouseId())
                    && dto.fromAreaId().equals(dto.toAreaId())
                    && dto.fromLocationId().equals(dto.toLocationId())) {
                throw new BusinessException("调出和调入库位不能相同");
            }
            TransferOrderItemEntity item = new TransferOrderItemEntity();
            item.setProductId(dto.productId());
            item.setFromWarehouseId(dto.fromWarehouseId());
            item.setFromAreaId(dto.fromAreaId());
            item.setFromLocationId(dto.fromLocationId());
            item.setToWarehouseId(dto.toWarehouseId());
            item.setToAreaId(dto.toAreaId());
            item.setToLocationId(dto.toLocationId());
            item.setQuantity(dto.quantity());
            item.setRemark(dto.remark());
            existing.addItem(item);
        }
        return transferOrderRepository.save(existing);
    }

    @Override
    public void submit(Long id, Long userId) {
        TransferOrderEntity order = getById(id);
        if (!"DRAFT".equals(order.getStatus())) {
            throw new BusinessException("只有草稿状态可提交");
        }
        order.setStatus("PENDING");
        order.setSubmittedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        transferOrderRepository.save(order);
    }

    @Override
    public void audit(Long id, String status, String auditRemark, Long userId) {
        TransferOrderEntity order = getById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("只有待审核状态可审核");
        }
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new BusinessException("审核状态无效");
        }
        if ("APPROVED".equals(status)) {
            // 审核通过时锁定调出库存，防止被其他出库单使用
            for (TransferOrderItemEntity item : order.getItems()) {
                StockCommand cmd = new StockCmd(item.getProductId(), item.getFromWarehouseId(),
                        item.getFromAreaId(), item.getFromLocationId(), item.getQuantity());
                stockService.lockOutbound(cmd, order.getOrderNo(), userId);
            }
        }
        order.setStatus(status);
        order.setAuditedBy(userId);
        order.setAuditedAt(LocalDateTime.now());
        order.setAuditRemark(auditRemark);
        order.setUpdatedBy(userId);
        transferOrderRepository.save(order);
    }

    @Override
    public void execute(Long id, Long userId) {
        TransferOrderEntity order = getById(id);
        if (!"APPROVED".equals(order.getStatus())) {
            throw new BusinessException("只有已审核状态可执行调拨");
        }
        for (TransferOrderItemEntity item : order.getItems()) {
            // 扣减调出库存（确认出库）
            StockCommand outCmd = new StockCmd(item.getProductId(), item.getFromWarehouseId(),
                    item.getFromAreaId(), item.getFromLocationId(), item.getQuantity());
            stockService.confirmOutbound(outCmd, order.getOrderNo(), userId);

            // 增加调入库存（确认入库）
            StockCommand inCmd = new StockCmd(item.getProductId(), item.getToWarehouseId(),
                    item.getToAreaId(), item.getToLocationId(), item.getQuantity());
            stockService.confirmInbound(inCmd, order.getOrderNo(), userId);
        }
        order.setStatus("COMPLETED");
        order.setCompletedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        transferOrderRepository.save(order);
    }

    @Override
    public void cancel(Long id, Long userId) {
        TransferOrderEntity order = getById(id);
        if ("COMPLETED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }
        // 如果已审核通过，释放锁定的库存
        if ("APPROVED".equals(order.getStatus())) {
            for (TransferOrderItemEntity item : order.getItems()) {
                StockCommand cmd = new StockCmd(item.getProductId(), item.getFromWarehouseId(),
                        item.getFromAreaId(), item.getFromLocationId(), item.getQuantity());
                stockService.cancelOutbound(cmd, order.getOrderNo(), userId);
            }
        }
        order.setStatus("CANCELLED");
        order.setUpdatedBy(userId);
        transferOrderRepository.save(order);
    }

    private String generateOrderNo() {
        String prefix = "TR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-%";
        int maxSeq = transferOrderRepository.findMaxSeq(prefix);
        return "TR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%06d", maxSeq + 1);
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}
