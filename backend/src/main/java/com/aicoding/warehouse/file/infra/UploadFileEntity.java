package com.aicoding.warehouse.file.infra;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_resource")
public class UploadFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;

    @Column(name = "file_type", length = 64)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "oss_key", length = 512, nullable = false)
    private String ossKey;

    @Column(name = "bucket_name", length = 128)
    private String bucketName;

    @Column(name = "business_type", length = 64)
    private String businessType;

    @Column(name = "business_id")
    private Long businessId;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted")
    private Integer deleted = 0;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getOssKey() { return ossKey; }
    public void setOssKey(String ossKey) { this.ossKey = ossKey; }
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public Long getBusinessId() { return businessId; }
    public void setBusinessId(Long businessId) { this.businessId = businessId; }
    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
