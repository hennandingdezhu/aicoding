package com.tokenscope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@Transactional
class StatsServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private StatsService statsService;

    private UsageRecord createRecord(int promptTokens, int completionTokens, double costValue) {
        UsageRecord record = new UsageRecord();
        record.setPromptTokens(promptTokens);
        record.setCompletionTokens(completionTokens);
        record.setCost(new BigDecimal(String.valueOf(costValue)));
        return recordService.create(record);
    }

    @Test
    void statsShouldShowCurrentMonthCost() {
        createRecord(100, 200, 1.50);
        createRecord(300, 400, 2.50);

        Map<String, Object> stats = statsService.getStats();

        BigDecimal currentCost = (BigDecimal) stats.get("currentCost");
        assertThat(currentCost).isCloseTo(new BigDecimal("4.00"), within(new BigDecimal("0.01")));
    }

    @Test
    void budgetPercentageCalculation() {
        createRecord(1000, 2000, 30.00);

        Map<String, Object> stats = statsService.getStats();

        BigDecimal percentage = (BigDecimal) stats.get("percentage");
        assertThat(percentage).isCloseTo(new BigDecimal("60.00"), within(new BigDecimal("0.10")));
    }
}