package com.tokenscope;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokenscope/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public BudgetConfig get() {
        return budgetService.get();
    }

    @PutMapping
    public ResponseEntity<BudgetConfig> update(@RequestBody BudgetConfig config) {
        BudgetConfig updated = budgetService.update(config);
        return ResponseEntity.ok(updated);
    }
}