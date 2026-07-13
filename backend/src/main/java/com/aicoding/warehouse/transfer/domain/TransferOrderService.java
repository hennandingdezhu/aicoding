package com.aicoding.warehouse.transfer.domain;

import com.aicoding.warehouse.transfer.infra.TransferOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferOrderService {

    Page<TransferOrderEntity> list(String status, Pageable pageable);

    TransferOrderEntity getById(Long id);

    TransferOrderEntity create(TransferOrderEntity order, List<TransferOrderItemDTO> items, Long userId);

    TransferOrderEntity update(Long id, TransferOrderEntity order, List<TransferOrderItemDTO> items, Long userId);

    void submit(Long id, Long userId);

    void audit(Long id, String status, String auditRemark, Long userId);

    void execute(Long id, Long userId);

    void cancel(Long id, Long userId);

    record TransferOrderItemDTO(Long productId, Long fromWarehouseId, Long fromAreaId, Long fromLocationId,
                                 Long toWarehouseId, Long toAreaId, Long toLocationId,
                                 java.math.BigDecimal quantity, String remark) {}
}