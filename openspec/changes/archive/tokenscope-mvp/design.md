# TokenScope MVP — Design

## 架构

```
frontend (Vue 3 + ECharts, port 3000)
       ↓ REST /api/*
backend (Spring Boot, port 8080)
       ↓ JPA
MySQL / H2 (test)
       
python/                          ← Python 辅助
  token_counter.py               ← tiktoken 精准计算
```

## 数据模型

### usage_record（消耗记录）
| 字段 | 类型 | 说明 |
|---|---|---|
| id | BIGINT PK | |
| model_name | VARCHAR(64) | deepseek-v4-pro |
| prompt_tokens | INT | 输入 token |
| completion_tokens | INT | 输出 token |
| total_tokens | INT | prompt+completion |
| cost | DECIMAL(10,6) | 费用 $ |
| description | VARCHAR(255) | 用途说明 |
| created_at | DATETIME | |

### budget_config（预算配置）
| 字段 | 类型 | 说明 |
|---|---|---|
| id | BIGINT PK | |
| monthly_budget | DECIMAL(10,2) | 月度预算 $ |
| alert_threshold | DECIMAL(3,2) | 告警比例 0.0-1.0 |
| updated_at | DATETIME | |

## API

| 方法 | 端点 | 说明 |
|---|---|---|
| GET | /api/tokenscope/records | 消耗记录列表(分页) |
| POST | /api/tokenscope/records | 新增消耗记录 |
| DELETE | /api/tokenscope/records/{id} | 删除记录 |
| GET | /api/tokenscope/budget | 获取预算配置 |
| PUT | /api/tokenscope/budget | 更新预算配置 |
| GET | /api/tokenscope/stats | 月度统计(current/cost/budget/%) |
| GET | /api/tokenscope/daily-trend | 每日消耗趋势(图表) |

## 计价规则 (MVP)
| 模型 | 输入 $/1M token | 输出 $/1M token |
|---|---|---|
| deepseek-v4-pro | $1.10 | $4.40 |

## 前端页面

1. **仪表盘** `/`: 月度总览卡片 + 每日趋势折线图 + 预算进度条
2. **消耗记录** `/records`: 表格 + 新增按钮 + 删除
3. **设置** `/settings`: 预算/告警阈值编辑

## 技术选型
- 后端: Spring Boot 3.2.5, JPA, H2(test), MySQL(prod)
- 前端: Vue 3, ECharts, Axios
- Python: tiktoken (独立脚本，API 调用)
- 测试: JUnit 5, AssertJ