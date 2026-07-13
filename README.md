# 电子仓储管理系统 (Warehouse Management System)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.4-4FC08D)](https://vuejs.org/)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

面向中小型企业的 Web 版仓储管理系统，支持多仓库、多库位、完整的入库/出库/调拨/盘点业务流程。

---

## 📸 功能概览

| 模块 | 功能 |
|---|---|
| 🔐 登录认证 | JWT Token 鉴权，角色权限控制 |
| 📦 商品管理 | 商品 CRUD、分类树、供应商、客户 |
| 🏗️ 仓库管理 | 多仓库、库区、库位管理 |
| 📥 入库管理 | 采购入库，提交审核→审核→确认入库，自动增加库存 |
| 📤 出库管理 | 销售出库，审核锁库存→确认扣库存→取消释放库存 |
| 🔄 调拨管理 | 仓库间/库位间调拨，锁库存→确认移动 |
| 📋 盘点管理 | 自动生成账面库存→录入实盘→差异调整 |
| 📊 库存管理 | 实时库存、库存流水、库存预警 |
| 📈 报表中心 | 库存报表、入库/出库统计、盘点差异、调拨统计 |
| 👤 系统管理 | 用户、角色、权限、操作日志 |
| 📎 文件管理 | 业务附件上传（本地/OSS） |

## 🏛️ 项目架构

```
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/aicoding/warehouse/
│   │   ├── common/             # 公共组件 (ApiResponse, JWT, Security, 异常处理)
│   │   ├── auth/               # 登录认证模块
│   │   ├── user/               # 用户管理模块
│   │   ├── role/               # 角色管理模块
│   │   ├── permission/         # 权限管理模块
│   │   ├── warehouse/          # 仓库管理模块
│   │   ├── warehousearea/      # 库区管理模块
│   │   ├── warehouselocation/  # 库位管理模块
│   │   ├── product/            # 商品管理模块
│   │   ├── productcategory/    # 商品分类模块
│   │   ├── supplier/           # 供应商管理模块
│   │   ├── customer/           # 客户管理模块
│   │   ├── inbound/            # 入库管理模块
│   │   ├── outbound/           # 出库管理模块
│   │   ├── transfer/           # 调拨管理模块
│   │   ├── inventory/          # 盘点管理模块
│   │   ├── stock/              # 库存领域模型 + JPA + API
│   │   ├── file/               # 文件管理模块
│   │   ├── report/             # 报表中心模块
│   │   └── log/                # 操作日志模块
│   └── src/test/               # 96 个测试用例
├── frontend/                   # Vue 3 前端
│   └── src/
│       ├── api/
│       │   ├── client.js       # Axios 实例 (Token 拦截)
│       │   ├── index.js        # API 切换层 (mock / real)
│       │   ├── mock/           # 模拟数据服务 (开发模式)
│       │   └── real/           # 真实 API 调用 (生产模式)
│       ├── components/         # 公共组件 (Layout 侧边栏)
│       ├── router/             # 路由配置 (30 条路由 + 登录守卫)
│       └── views/              # 25 个业务页面
├── database/
│   └── warehouse_schema.sql    # 完整 MySQL 建表脚本
└── docs/
    ├── RESTful接口文档.md        # 接口文档 (20 模块)
    └── 仓储系统PRD.md           # 产品需求文档
```

### 后端分层架构 (DDD 风格)

每个业务模块采用统一的三层结构：

```
{module}/
├── domain/    # 领域服务接口 (Port)
├── infra/     # JPA 实体、仓库、服务实现 (Adapter)
└── web/       # REST Controller、DTO
```

### 库存领域模型

核心库存采用充血模型设计，确保数据一致性：

```
Stock (领域实体)
├── increaseAvailable(qty)        # 入库 → total↑ available↑
├── lock(qty)                     # 审核出库 → available↓ locked↑
├── confirmLockedOutbound(qty)    # 确认出库 → locked↓ total↓
├── releaseLocked(qty)            # 取消出库 → locked↓ available↑
└── decreaseAvailable(qty)        # 盘点盘亏 → available↓ total↓

状态公式: total = available + locked
```

## 🚀 快速开始

### 环境要求

- JDK 21+ 和 Maven 3.9+
- Node.js 20+ 和 npm
- MySQL 8.0+

### 1. 创建数据库

```bash
mysql -u root -p < database/warehouse_schema.sql
```

### 2. 启动后端

```bash
cd backend
# 复制并修改数据库配置
cp src/main/resources/application.example.yml src/main/resources/application.yml
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 `http://localhost:3000`

### 4. 登录

- 默认使用模拟数据模式，无需后端即可查看效果
- 演示账号: `admin` / `admin123`
- 连接真实后端: `VITE_USE_MOCK=false npm run dev`

### 切换 API 模式

```bash
# 开发模式 - 使用前端 Mock 数据（默认）
npm run dev

# 生产模式 - 连接真实后端
VITE_USE_MOCK=false npm run dev
```

## 🧪 测试

```bash
cd backend
mvn test -Dspring.profiles.active=test
```

- 96 个测试用例，覆盖所有业务模块
- 使用 H2 内存数据库，无需外部 MySQL

## 📡 API 接口

| 模块 | 端点 | 说明 |
|---|---|---|
| 认证 | `POST /api/auth/login` | 用户登录 |
| 用户 | `GET/POST/PUT/DELETE /api/users` | 用户 CRUD |
| 角色 | `GET/POST/PUT/DELETE /api/roles` | 角色 CRUD |
| 权限 | `GET /api/permissions/tree` | 权限树 |
| 商品 | `GET/POST/PUT/DELETE /api/products` | 商品 CRUD |
| 仓库 | `GET/POST/PUT/DELETE /api/warehouses` | 仓库 CRUD |
| 入库 | `GET/POST /api/inbound-orders` | 入库单管理 |
| 出库 | `GET/POST /api/outbound-orders` | 出库单管理 |
| 库存 | `GET /api/stocks` | 实时库存 |
| 调拨 | `GET/POST /api/transfer-orders` | 调拨单管理 |
| 盘点 | `GET/POST /api/inventory-checks` | 盘点单管理 |
| 报表 | `GET /api/reports/*` | 6 类业务报表 |

完整文档见 [RESTful接口文档.md](docs/RESTful接口文档.md)

## 📋 技术栈

| 层级 | 技术 |
|---|---|
| 后端框架 | Spring Boot 3.2.5 |
| 安全 | Spring Security + JWT (jjwt 0.12) |
| 持久层 | Spring Data JPA + Hibernate |
| 数据库 | MySQL 8.0 (生产) / H2 (测试) |
| 前端框架 | Vue 3 (Composition API) |
| 构建工具 | Vite 5 |
| 路由 | Vue Router 4 |
| HTTP 客户端 | Axios |
| 测试 | JUnit 5 + AssertJ + H2 |
| 构建 | Maven 3.9 |

## ⚠️ 注意事项

- 前端默认使用模拟数据，所有页面标注 "模拟数据" 横幅
- 生产部署前请修改 `application.yml` 中的 JWT secret 和数据库密码
- 系统不包含扫码、PDA、条码枪等硬件相关功能
- 文件上传默认使用本地存储，可扩展为 OSS

## 📄 License

MIT