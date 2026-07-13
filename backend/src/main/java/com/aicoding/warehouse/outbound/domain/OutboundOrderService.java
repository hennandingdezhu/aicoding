package com.aicoding.warehouse.outbound.domain;

import com.aicoding.warehouse.outbound.infra.OutboundOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OutboundOrderService {

    Page<OutboundOrderEntity> list(String status, Long warehouseId, Pageable pageable);

    OutboundOrderEntity getById(Long id);

    OutboundOrderEntity create(OutboundOrderEntity order, List<OutboundOrderItemDTO> items, Long userId);

    OutboundOrderEntity update(Long id, OutboundOrderEntity order, List<OutboundOrderItemDTO> items, Long userId);

    void submit(Long id, Long userId);

    void audit(Long id, String status, String auditRemark, Long userId);

    void confirm(Long id, Long userId);

    void cancel(Long id, Long userId);

    record OutboundOrderItemDTO(Long productId, Long areaId, Long locationId,
                                 java.math.BigDecimal quantity, java.math.BigDecimal unitPrice, String remark) {}
}