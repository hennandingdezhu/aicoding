package com.tokenscope;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenScopeIntegrationTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private BudgetService budgetService;

    @Test
    @Order(1)
    void createRecordWithPromptTextShouldAutoCountTokens() {
        UsageRecord record = new UsageRecord();
        record.setModelName("deepseek-v4-pro");
        record.setPrompt("Hello, this is a test prompt for token counting.");
        record.setCompletion("This is the completion response.");
        record.setDescription("Integration test");

        UsageRecord saved = recordService.create(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPromptTokens()).isGreaterThan(0);
        assertThat(saved.getCompletionTokens()).isGreaterThan(0);
        assertThat(saved.getTotalTokens()).isEqualTo(
                saved.getPromptTokens() + saved.getCompletionTokens());
        assertThat(saved.getCost()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @Order(2)
    void createRecordWithManualTokensShouldUseProvidedValues() {
        UsageRecord record = new UsageRecord();
        record.setModelName("deepseek-v4-pro");
        record.setPromptTokens(500);
        record.setCompletionTokens(300);
        record.setDescription("Manual token entry");

        UsageRecord saved = recordService.create(record);

        assertThat(saved.getPromptTokens()).isEqualTo(500);
        assertThat(saved.getCompletionTokens()).isEqualTo(300);
        assertThat(saved.getTotalTokens()).isEqualTo(800);
        assertThat(saved.getCost()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @Order(3)
    void statsShouldReflectCreatedRecords() {
        createRecord(100, 200, 1.50);
        createRecord(300, 400, 2.50);

        Map<String, Object> stats = statsService.getStats();

        assertThat(stats.get("currentCost")).isNotNull();
        assertThat(stats.get("monthlyBudget")).isNotNull();
        assertThat(stats.get("percentage")).isNotNull();
        assertThat((BigDecimal) stats.get("percentage"))
                .isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    @Order(4)
    void budgetUpdateShouldWork() {
        BudgetConfig config = budgetService.get();
        BigDecimal original = config.getMonthlyBudget();

        config.setMonthlyBudget(new BigDecimal("100.00"));
        config.setAlertThreshold(new BigDecimal("0.90"));
        BudgetConfig updated = budgetService.update(config);

        assertThat(updated.getMonthlyBudget()).isEqualByComparingTo("100.00");
        assertThat(updated.getAlertThreshold()).isEqualByComparingTo("0.90");

        config.setMonthlyBudget(original);
        budgetService.update(config);
    }

    @Test
    @Order(5)
    void recordsPageShouldWork() {
        Page<UsageRecord> page = recordService.findAll(PageRequest.of(0, 10));
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Order(6)
    void dailyTrendShouldNotThrow() {
        var trend = statsService.getDailyTrend();
        assertThat(trend).isNotNull();
    }

    private UsageRecord createRecord(int promptTokens, int completionTokens, double costValue) {
        UsageRecord record = new UsageRecord();
        record.setPromptTokens(promptTokens);
        record.setCompletionTokens(completionTokens);
        record.setCost(new BigDecimal(String.valueOf(costValue)));
        return recordService.create(record);
    }
}