package com.tokenscope;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tokenscope/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public Page<UsageRecord> list(Pageable pageable) {
        return recordService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<UsageRecord> create(@RequestBody UsageRecord record) {
        UsageRecord saved = recordService.create(record);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        recordService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}