# TokenScope MVP — Tasks

## 1. Backend Infrastructure
- [x] 1.1 Create Spring Boot project (web/jpa/h2/validation)
- [x] 1.2 UsageRecord JPA entity (id, modelName, promptTokens, completionTokens, totalTokens, cost, prompt, completion, description, createdAt)
- [x] 1.3 BudgetConfig JPA entity (id, monthlyBudget, alertThreshold, updatedAt)
- [x] 1.4 UsageRecordRepository extends JpaRepository
- [x] 1.5 BudgetConfigRepository extends JpaRepository
- [x] 1.6 data.sql init budget record

## 2. Backend API (TDD)
- [x] 2.1 Test: RecordService auto-calc totalTokens + cost
- [x] 2.2 Implement: RecordService + RecordController (CRUD)
- [x] 2.3 Test: BudgetService update budget
- [x] 2.4 Implement: BudgetService + BudgetController (GET/PUT)
- [x] 2.5 Test: StatsService monthly cost calculation
- [x] 2.6 Implement: StatsService + StatsController (stats, daily-trend)

## 3. Python Helper
- [x] 3.1 token_counter.py (tiktoken with model mapping)
- [x] 3.2 Test: token_counter.py correct token count
- [x] 3.3 Java TokenCounterService calling Python, with fallback estimation

## 4. Frontend
- [x] 4.1 Vue 3 + Vite project
- [x] 4.2 ECharts + axios
- [x] 4.3 Dashboard: monthly cards + budget progress bar + trend chart
- [x] 4.4 Records page: table + add/delete
- [x] 4.5 Settings page: budget config form
- [x] 4.6 API layer (axios, proxy to backend)

## 5. Integration Tests
- [x] 5.1 TokenScopeIntegrationTest: 6 e2e tests covering auto-count, manual tokens, stats, budget, pagination, daily trend
- [x] 5.2 Manual API verification: all 7 endpoints return correct responses