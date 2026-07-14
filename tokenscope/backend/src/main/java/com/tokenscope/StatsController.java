package com.tokenscope;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tokenscope/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public Map<String, Object> getStats() {
        return statsService.getStats();
    }

    @GetMapping("/daily-trend")
    public List<Map<String, Object>> getDailyTrend() {
        return statsService.getDailyTrend();
    }
}