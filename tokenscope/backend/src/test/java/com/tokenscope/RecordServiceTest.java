package com.tokenscope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;

@SpringBootTest
@Transactional
class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Test
    void createRecordShouldAutoCalculateTotalTokens() {
        UsageRecord record = new UsageRecord();
        record.setPromptTokens(100);
        record.setCompletionTokens(200);

        UsageRecord saved = recordService.create(record);

        assertThat(saved.getTotalTokens()).isEqualTo(300);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void createRecordShouldCalculateCost() {
        UsageRecord record = new UsageRecord();
        record.setPromptTokens(1000);
        record.setCompletionTokens(2000);

        UsageRecord saved = recordService.create(record);

        assertThat(saved.getCost()).isNotNull();
        assertThat(saved.getCost()).isCloseTo(new BigDecimal("0.0099"), byLessThan(new BigDecimal("0.0001")));
    }
}