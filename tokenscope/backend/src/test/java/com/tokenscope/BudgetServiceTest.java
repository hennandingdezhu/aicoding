package com.tokenscope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BudgetServiceTest {

    @Autowired
    private BudgetService budgetService;

    @Test
    void getBudgetShouldReturnDefault() {
        BudgetConfig budget = budgetService.get();

        assertThat(budget).isNotNull();
        assertThat(budget.getMonthlyBudget()).isEqualTo(new BigDecimal("50.00"));
        assertThat(budget.getAlertThreshold()).isEqualByComparingTo("0.80");
    }

    @Test
    void updateBudgetShouldPersist() {
        BudgetConfig config = budgetService.get();
        config.setMonthlyBudget(new BigDecimal("100.00"));

        BudgetConfig updated = budgetService.update(config);

        assertThat(updated.getMonthlyBudget()).isEqualTo(new BigDecimal("100.00"));

        BudgetConfig fetched = budgetService.get();
        assertThat(fetched.getMonthlyBudget()).isEqualTo(new BigDecimal("100.00"));
    }
}