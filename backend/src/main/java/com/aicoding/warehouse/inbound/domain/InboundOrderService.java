package com.aicoding.warehouse.inbound.domain;

import com.aicoding.warehouse.inbound.infra.InboundOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InboundOrderService {

    Page<InboundOrderEntity> list(String status, Long warehouseId, Pageable pageable);

    InboundOrderEntity getById(Long id);

    InboundOrderEntity create(InboundOrderEntity order, List<InboundOrderItemDTO> items, Long userId);

    InboundOrderEntity update(Long id, InboundOrderEntity order, List<InboundOrderItemDTO> items, Long userId);

    void submit(Long id, Long userId);

    void audit(Long id, String status, String auditRemark, Long userId);

    void confirm(Long id, Long userId);

    void cancel(Long id, Long userId);

    record InboundOrderItemDTO(Long productId, Long areaId, Long locationId,
                                java.math.BigDecimal quantity, java.math.BigDecimal unitPrice, String remark) {}
}