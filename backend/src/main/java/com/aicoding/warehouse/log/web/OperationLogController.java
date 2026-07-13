package com.aicoding.warehouse.log.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.common.PageResult;
import com.aicoding.warehouse.log.infra.OperationLogEntity;
import com.aicoding.warehouse.log.infra.OperationLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operation-logs")
public class OperationLogController {

    private final OperationLogRepository operationLogRepository;

    public OperationLogController(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @GetMapping
    public ApiResponse<PageResult<OperationLogEntity>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String operationType) {
        if (pageSize > 100) pageSize = 100;
        Page<OperationLogEntity> result = operationLogRepository.findWithFilters(moduleName, operationType, PageRequest.of(page - 1, pageSize));
        return ApiResponse.ok(new PageResult<>(result.getContent(), result.getTotalElements(), page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<OperationLogEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(operationLogRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "闁瑰灝绉崇紞鏃堝籍閵夈儳绠跺☉鎾崇Т閻°劑宕?)));
    }
}