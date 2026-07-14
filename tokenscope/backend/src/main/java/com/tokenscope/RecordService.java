package com.tokenscope;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RecordService {

    private final UsageRecordRepository repository;
    private final TokenCounterService tokenCounter;

    @Value("${tokenscope.input-price}")
    private double inputPrice;

    @Value("${tokenscope.output-price}")
    private double outputPrice;

    public RecordService(UsageRecordRepository repository, TokenCounterService tokenCounter) {
        this.repository = repository;
        this.tokenCounter = tokenCounter;
    }

    @Transactional
    public UsageRecord create(UsageRecord record) {
        String promptText = record.getPrompt();
        String completionText = record.getCompletion();

        if (record.getPromptTokens() == null && promptText != null && !promptText.isEmpty()) {
            record.setPromptTokens(tokenCounter.countTokens(promptText, record.getModelName()));
        }
        if (record.getCompletionTokens() == null && completionText != null && !completionText.isEmpty()) {
            record.setCompletionTokens(tokenCounter.countTokens(completionText, record.getModelName()));
        }
        if (record.getPromptTokens() == null) record.setPromptTokens(0);
        if (record.getCompletionTokens() == null) record.setCompletionTokens(0);

        if (record.getCost() == null) {
            double inputCost = (record.getPromptTokens() / 1_000_000.0) * inputPrice;
            double outputCost = (record.getCompletionTokens() / 1_000_000.0) * outputPrice;
            BigDecimal cost = BigDecimal.valueOf(inputCost + outputCost)
                    .setScale(6, RoundingMode.HALF_UP);
            record.setCost(cost);
        }
        return repository.save(record);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UsageRecord> findAll(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }
}