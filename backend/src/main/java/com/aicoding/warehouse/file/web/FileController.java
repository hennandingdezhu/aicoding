package com.aicoding.warehouse.file.web;

import com.aicoding.warehouse.common.ApiResponse;
import com.aicoding.warehouse.common.BusinessException;
import com.aicoding.warehouse.file.infra.UploadFileEntity;
import com.aicoding.warehouse.file.infra.UploadFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final UploadFileRepository uploadFileRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public FileController(UploadFileRepository uploadFileRepository) {
        this.uploadFileRepository = uploadFileRepository;
    }

    @PostMapping
    public ApiResponse<UploadFileEntity> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "businessId", required = false) Long businessId,
            @AuthenticationPrincipal Long userId) throws IOException {
        // 文件类型白名单
        String contentType = file.getContentType();
        String originalName = file.getOriginalFilename();
        if (originalName != null && !originalName.matches(".*\\.(jpg|jpeg|png|gif|pdf|doc|docx|xls|xlsx|csv|txt|zip)$")) {
            throw new BusinessException("不支持的文件类型");
        }
        // 限制文件大小 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BusinessException("文件大小不能超过10MB");
        }

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = dir.resolve(storedName);
        file.transferTo(target.toFile());

        UploadFileEntity entity = new UploadFileEntity();
        entity.setOriginalName(file.getOriginalFilename());
        entity.setFileType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setOssKey(storedName);
        entity.setBucketName(uploadDir);
        entity.setBusinessType(businessType);
        entity.setBusinessId(businessId);
        entity.setUploadedBy(userId);

        return ApiResponse.ok(uploadFileRepository.save(entity));
    }

    @GetMapping("/{id}")
    public ApiResponse<UploadFileEntity> getById(@PathVariable Long id) {
        return ApiResponse.ok(uploadFileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文件不存在")));
    }

    @GetMapping("/{id}/url")
    public ApiResponse<String> getUrl(@PathVariable Long id) {
        UploadFileEntity file = uploadFileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文件不存在"));
        return ApiResponse.ok("/api/files/" + id + "/download");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        UploadFileEntity file = uploadFileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "文件不存在"));
        uploadFileRepository.delete(file);
        return ApiResponse.ok();
    }
}