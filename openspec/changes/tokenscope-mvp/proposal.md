# TokenScope MVP — Proposal

## 概述
AI 开发者需要知道自己每天/每月花了多少 API token，用于控制成本和优化 prompt。TokenScope 是一个轻量级的 AI Token 用量追踪工具。

## 问题
- 使用 AI API 不知道花了多少钱
- 想做预算控制但没有工具
- prompt 优化后看不到 token 消耗变化

## 解决方案
TokenScope 提供：
1. 记录每次 API 调用的 token 消耗
2. 月度预算设定 + 超限告警
3. ECharts 可视化仪表盘

## MVP 范围
- 单一模型（deepseek-v4-pro）
- 单一用户
- 手动录入消耗
- 预算对比看板
- 3 个页面：仪表盘、消耗记录、设置

## 非 MVP
- 多模型定价
- 多用户
- 实时 API 代理
- 告警通知