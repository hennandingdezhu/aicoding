package com.aicoding.warehouse.inbound.infra;

import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.inbound.domain.InboundOrderService;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InboundOrderServiceImpl implements InboundOrderService {

    private final InboundOrderRepository inboundOrderRepository;
    private final StockService stockService;

    public InboundOrderServiceImpl(InboundOrderRepository inboundOrderRepository, StockService stockService) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InboundOrderEntity> list(String status, Long warehouseId, Pageable pageable) {
        return inboundOrderRepository.findWithFilters(status, warehouseId, pageable);
    }

    @Override
    public InboundOrderEntity getById(Long id) {
        return inboundOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "入库单不存在"));
    }

    @Override
    public InboundOrderEntity create(InboundOrderEntity order, List<InboundOrderItemDTO> items, Long userId) {
        order.setStatus("DRAFT");
        order.setCreatedBy(userId);
        order.setOrderNo(generateOrderNo());
        for (InboundOrderItemDTO dto : items) {
            InboundOrderItemEntity item = new InboundOrderItemEntity();
            item.setProductId(dto.productId());
            item.setAreaId(dto.areaId());
            item.setLocationId(dto.locationId());
            item.setQuantity(dto.quantity());
            item.setUnitPrice(dto.unitPrice());
            item.setRemark(dto.remark());
            order.addItem(item);
        }
        return inboundOrderRepository.save(order);
    }

    @Override
    public InboundOrderEntity update(Long id, InboundOrderEntity order, List<InboundOrderItemDTO> items, Long userId) {
        InboundOrderEntity existing = getById(id);
        if (!"DRAFT".equals(existing.getStatus())) {
            throw new BusinessException("只有草稿状态可编辑");
        }
        existing.setWarehouseId(order.getWarehouseId());
        existing.setSupplierId(order.getSupplierId());
        existing.setInboundType(order.getInboundType());
        existing.setRemark(order.getRemark());
        existing.setUpdatedBy(userId);
        existing.getItems().clear();
        for (InboundOrderItemDTO dto : items) {
            InboundOrderItemEntity item = new InboundOrderItemEntity();
            item.setProductId(dto.productId());
            item.setAreaId(dto.areaId());
            item.setLocationId(dto.locationId());
            item.setQuantity(dto.quantity());
            item.setUnitPrice(dto.unitPrice());
            item.setRemark(dto.remark());
            existing.addItem(item);
        }
        return inboundOrderRepository.save(existing);
    }

    @Override
    public void submit(Long id, Long userId) {
        InboundOrderEntity order = getById(id);
        if (!"DRAFT".equals(order.getStatus())) {
            throw new BusinessException("只有草稿状态可提交");
        }
        order.setStatus("PENDING");
        order.setSubmittedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        inboundOrderRepository.save(order);
    }

    @Override
    public void audit(Long id, String status, String auditRemark, Long userId) {
        InboundOrderEntity order = getById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("只有待审核状态可审核");
        }
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new BusinessException("审核状态无效");
        }
        order.setStatus(status);
        order.setAuditedBy(userId);
        order.setAuditedAt(LocalDateTime.now());
        order.setAuditRemark(auditRemark);
        order.setUpdatedBy(userId);
        inboundOrderRepository.save(order);
    }

    @Override
    public void confirm(Long id, Long userId) {
        InboundOrderEntity order = getById(id);
        if (!"APPROVED".equals(order.getStatus())) {
            throw new BusinessException("只有已审核状态可确认入库");
        }
        for (InboundOrderItemEntity item : order.getItems()) {
            StockCommand cmd = new StockCmd(item.getProductId(), order.getWarehouseId(),
                    item.getAreaId(), item.getLocationId(), item.getQuantity());
            stockService.confirmInbound(cmd, order.getOrderNo(), userId);
        }
        order.setStatus("CONFIRMED");
        order.setConfirmedAt(LocalDateTime.now());
        order.setUpdatedBy(userId);
        inboundOrderRepository.save(order);
    }

    @Override
    public void cancel(Long id, Long userId) {
        InboundOrderEntity order = getById(id);
        if ("CONFIRMED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }
        order.setStatus("CANCELLED");
        order.setUpdatedBy(userId);
        inboundOrderRepository.save(order);
    }

    private String generateOrderNo() {
        String prefix = "IN-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-%";
        int maxSeq = inboundOrderRepository.findMaxSeq(prefix);
        return "IN-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%06d", maxSeq + 1);
    }

    record StockCmd(Long productId, Long warehouseId, Long areaId, Long locationId, BigDecimal quantity) implements StockCommand {}
}