package com.tokenscope;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BudgetService {

    private final BudgetConfigRepository repository;

    public BudgetService(BudgetConfigRepository repository) {
        this.repository = repository;
    }

    public BudgetConfig get() {
        return repository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("BudgetConfig not found"));
    }

    @Transactional
    public BudgetConfig update(BudgetConfig config) {
        BudgetConfig existing = get();
        existing.setMonthlyBudget(config.getMonthlyBudget());
        existing.setAlertThreshold(config.getAlertThreshold());
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }
}