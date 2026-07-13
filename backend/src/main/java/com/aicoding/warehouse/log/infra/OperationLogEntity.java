package com.aicoding.warehouse.log.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation_log")
public class OperationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "operator_name", length = 64)
    private String operatorName;

    @Column(name = "module_name", length = 64)
    private String moduleName;

    @Column(name = "operation_type", length = 64)
    private String operationType;

    @Column(name = "operation_content", columnDefinition = "TEXT")
    private String operationContent;

    @Column(name = "request_uri", length = 255)
    private String requestUri;

    @Column(name = "request_method", length = 16)
    private String requestMethod;

    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public OperationLogEntity() {}
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationContent() { return operationContent; }
    public void setOperationContent(String operationContent) { this.operationContent = operationContent; }
    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }
    public String getRequestMethod() { return requestMethod; }
    public void setRequestMethod(String requestMethod) { this.requestMethod = requestMethod; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}