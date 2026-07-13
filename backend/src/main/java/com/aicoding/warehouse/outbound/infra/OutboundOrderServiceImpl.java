package com.aicoding.warehouse.outbound.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.outbound.domain.OutboundOrderService;
import com.aicoding.warehouse.stock.domain.StockCommand;
import com.aicoding.warehouse.stock.domain.StockService;
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
public class OutboundOrderServiceImpl implements OutboundOrderService {

    private final OutboundOrderRepository outboundOrderRepository;
    private final StockService stockService;

    public OutboundOrderServiceImpl(OutboundOrderRepository outboundOrderRepository, StockService stockService) {
        this.outboundOrderRepository = outboundOrderRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutboundOrderEntity> list(String status, Long warehouseId, Pageable pageable) {
        return outboundOrderRepository.findWithFilters(status, warehouseId, pageable);
    }

    @Override
    public OutboundOrderEntity getById(Long id) {
        return outboundOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "出库单不存在"));
    }

    @Override
    public OutboundOrderEntity create(OutboundOrderEntity order, List<OutboundOrderItemDTO> items, Long userId) {
        order.setStatus("DRAFT");
        order.setCreatedBy(userId);
        order.setOrderNo(generateOrderNo());
        for (OutboundOrderItemDTO dto : items) {
            OutboundOrderItemEntity item = new OutboundOrderItemEntity();
            item.setProductId(dto.productId());
            item.setAreaId(dto.areaId());
            item.setLocationId(dto.locationId());
            item.setQuantity(dto.quantity());
            item.setUnitPrice(dto.unitPrice());
            item.setRemark(dto.remark());
            order.addItem(item);
        }
        return outboundOrderRepository.save(order);
    }

    @Override
    public OutboundOrderEntity update(Long id, OutboundOrderEntity order, List<OutboundOrderItemDTO> items, Long userId) {
        OutboundOrderEntity existing = getById(id);
        if (!"DRAFT".equals(existing.getStatus())) {
            throw new BusinessException("只有草稿状态可编辑");
        }
        existing.setWarehouseId(order.getWarehouseId());
        existing.setCustomerId(order.getCustomerId());
        existing.setOutboundType(order.getOutboundType());
        existing.setRemark(order.getRemark());
        existing.setUpdatedBy(userId);
        existing.getItems().clear();
        for (OutboundOrderItemDTO dto : items) {
            OutboundOrderItemEntity item = new OutboundOrderItemEntity();
            item.setProductId(dto.productId());
            item.setAreaId(dto.areaId());
            item.setLocationId(dto.locationId());
            item.setQuantity(dto.quantity());
            item.setUnitPrice(dto.unitPrice());
            item.setRemark(dto.remark());
            existing.addItem(item);
        }
        return outboundOrderRepository.save(existing);
    }

    @Override
    public void submit(Long id, Long userId) {
        OutboundOrderEntity order = getById(id);
        if (!"DRAFT".equals(order.getStatus())) {
            throw new BusinessException("只有草稿状态可提交");
        }
        order.setStatus("PENDING");
        order.setSubmittedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        outboundOrderRepository.save(order);
    }

    @Override
    public void audit(Long id, String status, String auditRemark, Long userId) {
        OutboundOrderEntity order = getById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("只有待审核状态可审核");
        }
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new BusinessException("审核状态无效");
        }
        if ("APPROVED".equals(status)) {
            for (OutboundOrderItemEntity item : order.getItems()) {
                StockCommand cmd = new StockCmd(item.getProductId(), order.getWarehouseId(),
                        item.getAreaId(), item.getLocationId(), item.getQuantity());
                stockService.lockOutbound(cmd, order.getOrderNo(), userId);
            }
        }
        order.setStatus(status);
        order.setAuditedBy(userId);
        order.setAuditedAt(LocalDateTime.now());
        order.setAuditRemark(auditRemark);
        order.setUpdatedBy(userId);
        outboundOrderRepository.save(order);
    }

    @Override
    public void confirm(Long id, Long userId) {
        OutboundOrderEntity order = getById(id);
        if (!"APPROVED".equals(order.getStatus())) {
            throw new BusinessException("只有已审核状态可确认出库");
        }
        for (OutboundOrderItemEntity item : order.getItems()) {
            StockCommand cmd = new StockCmd(item.getProductId(), order.getWarehouseId(),
                    item.getAreaId(), item.getLocationId(), item.getQuantity());
            stockService.confirmOutbound(cmd, order.getOrderNo(), userId);
        }
        order.setStatus("CONFIRMED");
        order.setConfirmedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        outboundOrderRepository.save(order);
    }

    @Override
    public void cancel(Long id, Long userId) {
        OutboundOrderEntity order = getById(id);
        if ("CONFIRMED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }
        if ("APPROVED".equals(order.getStatus())) {
            for (OutboundOrderItemEntity item : order.getItems()) {
                StockCommand cmd = new StockCmd(item.getProductId(), order.getWarehouseId(),
                        item.getAreaId(), item.getLocationId(), item.getQuantity());
                stockService.cancelOutbound(cmd, order.getOrderNo(), userId);
            }
        }
        order.setStatus("CANCELLED");
        order.setUpdatedBy(userId);
        outboundOrderRepository.save(order);
    }

    private String generateOrderNo() {
        String prefix = "OUT-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-%";
        int maxSeq = outboundOrderRepository.findMaxSeq(prefix);
        return "OUT-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%06d", maxSeq + 1);
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}