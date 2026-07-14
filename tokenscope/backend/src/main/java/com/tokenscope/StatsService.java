package com.tokenscope;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StatsService {

    private final EntityManager entityManager;
    private final BudgetService budgetService;

    public StatsService(EntityManager entityManager, BudgetService budgetService) {
        this.entityManager = entityManager;
        this.budgetService = budgetService;
    }

    public Map<String, Object> getStats() {
        BudgetConfig budget = budgetService.get();
        BigDecimal monthlyBudget = budget.getMonthlyBudget();
        BigDecimal alertThreshold = budget.getAlertThreshold();

        Query query = entityManager.createNativeQuery(
                "SELECT COALESCE(SUM(cost), 0) FROM usage_record WHERE MONTH(created_at) = MONTH(NOW()) AND YEAR(created_at) = YEAR(NOW())");
        BigDecimal currentCost = (BigDecimal) query.getSingleResult();
        currentCost = currentCost.setScale(2, RoundingMode.HALF_UP);

        BigDecimal percentage = monthlyBudget.compareTo(BigDecimal.ZERO) > 0
                ? currentCost.divide(monthlyBudget, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        boolean isOverBudget = currentCost.compareTo(monthlyBudget) > 0;

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("currentCost", currentCost);
        stats.put("monthlyBudget", monthlyBudget);
        stats.put("percentage", percentage);
        stats.put("alertThreshold", alertThreshold);
        stats.put("isOverBudget", isOverBudget);
        return stats;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDailyTrend() {
        Query query = entityManager.createNativeQuery(
                "SELECT CAST(created_at AS DATE) as date, COALESCE(SUM(total_tokens), 0) as total_tokens, COALESCE(SUM(cost), 0) as cost " +
                "FROM usage_record " +
                "GROUP BY CAST(created_at AS DATE) " +
                "ORDER BY CAST(created_at AS DATE)");

        List<Object[]> rows = query.getResultList();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", row[0].toString());
            item.put("totalTokens", ((Number) row[1]).longValue());
            item.put("cost", row[2]);
            result.add(item);
        }

        return result;
    }
}