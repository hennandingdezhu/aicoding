# 电子仓储管理系统 RESTful 接口文档

## 1. 接口约定

### 1.1 基础信息

| 项目 | 说明 |
|---|---|
| 接口风格 | RESTful API |
| 数据格式 | JSON |
| 鉴权方式 | Bearer Token |
| 基础路径 | `/api` |
| 文件上传 | `multipart/form-data` |

### 1.2 通用请求头

```http
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### 1.3 通用响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 1.4 分页响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "page": 1,
    "pageSize": 20
  }
}
```

### 1.5 通用分页参数

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| page | number | 否 | 页码，默认 1 |
| pageSize | number | 否 | 每页条数，默认 20 |

### 1.6 通用状态码

| code | 说明 |
|---|---|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或 Token 失效 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 业务冲突 |
| 500 | 系统异常 |

## 2. 登录认证

### 2.1 用户登录

```http
POST /api/auth/login
```

请求体：

```json
{
  "username": "admin",
  "password": "123456"
}
```

响应：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "token",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "user": {
      "id": 1,
      "username": "admin",
      "realName": "管理员"
    }
  }
}
```

### 2.2 用户登出

```http
POST /api/auth/logout
```

### 2.3 当前用户信息

```http
GET /api/auth/me
```

### 2.4 修改密码

```http
PUT /api/auth/password
```

请求体：

```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

## 3. 用户管理

### 3.1 查询用户列表

```http
GET /api/users?page=1&pageSize=20&keyword=admin&status=1
```

### 3.2 查询用户详情

```http
GET /api/users/{id}
```

### 3.3 新增用户

```http
POST /api/users
```

请求体：

```json
{
  "username": "zhangsan",
  "password": "123456",
  "realName": "张三",
  "phone": "13800000000",
  "email": "zhangsan@example.com",
  "roleIds": [1, 2],
  "warehouseIds": [1]
}
```

### 3.4 编辑用户

```http
PUT /api/users/{id}
```

### 3.5 删除用户

```http
DELETE /api/users/{id}
```

### 3.6 启用/禁用用户

```http
PATCH /api/users/{id}/status
```

请求体：

```json
{
  "status": 1
}
```

### 3.7 重置密码

```http
PUT /api/users/{id}/password/reset
```

## 4. 角色与权限

### 4.1 查询角色列表

```http
GET /api/roles?page=1&pageSize=20&keyword=admin
```

### 4.2 新增角色

```http
POST /api/roles
```

请求体：

```json
{
  "roleCode": "warehouse_manager",
  "roleName": "仓库主管",
  "description": "负责仓库审核和管理"
}
```

### 4.3 编辑角色

```http
PUT /api/roles/{id}
```

### 4.4 删除角色

```http
DELETE /api/roles/{id}
```

### 4.5 查询权限树

```http
GET /api/permissions/tree
```

### 4.6 给角色分配权限

```http
PUT /api/roles/{id}/permissions
```

请求体：

```json
{
  "permissionIds": [1, 2, 3]
}
```

## 5. 商品管理

### 5.1 查询商品列表

```http
GET /api/products?page=1&pageSize=20&keyword=键盘&categoryId=1&status=1
```

### 5.2 查询商品详情

```http
GET /api/products/{id}
```

### 5.3 新增商品

```http
POST /api/products
```

请求体：

```json
{
  "productCode": "SKU001",
  "productName": "机械键盘",
  "categoryId": 1,
  "specification": "87键",
  "unit": "个",
  "brand": "Demo",
  "costPrice": 100.00,
  "salePrice": 199.00,
  "imageFileId": 1,
  "remark": "测试商品"
}
```

### 5.4 编辑商品

```http
PUT /api/products/{id}
```

### 5.5 删除商品

```http
DELETE /api/products/{id}
```

### 5.6 启用/禁用商品

```http
PATCH /api/products/{id}/status
```

### 5.7 商品导入

```http
POST /api/products/import
```

### 5.8 商品导出

```http
GET /api/products/export
```

## 6. 商品分类

### 6.1 查询分类树

```http
GET /api/product-categories/tree
```

### 6.2 新增分类

```http
POST /api/product-categories
```

### 6.3 编辑分类

```http
PUT /api/product-categories/{id}
```

### 6.4 删除分类

```http
DELETE /api/product-categories/{id}
```

## 7. 供应商管理

### 7.1 查询供应商列表

```http
GET /api/suppliers?page=1&pageSize=20&keyword=供应商
```

### 7.2 查询供应商详情

```http
GET /api/suppliers/{id}
```

### 7.3 新增供应商

```http
POST /api/suppliers
```

### 7.4 编辑供应商

```http
PUT /api/suppliers/{id}
```

### 7.5 删除供应商

```http
DELETE /api/suppliers/{id}
```

## 8. 客户管理

### 8.1 查询客户列表

```http
GET /api/customers?page=1&pageSize=20&keyword=客户
```

### 8.2 查询客户详情

```http
GET /api/customers/{id}
```

### 8.3 新增客户

```http
POST /api/customers
```

### 8.4 编辑客户

```http
PUT /api/customers/{id}
```

### 8.5 删除客户

```http
DELETE /api/customers/{id}
```

## 9. 仓库管理

### 9.1 查询仓库列表

```http
GET /api/warehouses?page=1&pageSize=20&keyword=上海&status=1
```

### 9.2 查询仓库详情

```http
GET /api/warehouses/{id}
```

### 9.3 新增仓库

```http
POST /api/warehouses
```

请求体：

```json
{
  "warehouseCode": "WH001",
  "warehouseName": "上海一号仓",
  "address": "上海市",
  "managerName": "李四",
  "managerPhone": "13800000001"
}
```

### 9.4 编辑仓库

```http
PUT /api/warehouses/{id}
```

### 9.5 删除仓库

```http
DELETE /api/warehouses/{id}
```

## 10. 库区管理

### 10.1 查询库区列表

```http
GET /api/warehouse-areas?warehouseId=1&page=1&pageSize=20
```

### 10.2 新增库区

```http
POST /api/warehouse-areas
```

### 10.3 编辑库区

```http
PUT /api/warehouse-areas/{id}
```

### 10.4 删除库区

```http
DELETE /api/warehouse-areas/{id}
```

## 11. 库位管理

### 11.1 查询库位列表

```http
GET /api/locations?warehouseId=1&areaId=1&page=1&pageSize=20
```

### 11.2 查询库位详情

```http
GET /api/locations/{id}
```

### 11.3 新增库位

```http
POST /api/locations
```

请求体：

```json
{
  "warehouseId": 1,
  "areaId": 1,
  "locationCode": "A-01-01",
  "locationName": "A区01货架01位",
  "capacity": 1000
}
```

### 11.4 编辑库位

```http
PUT /api/locations/{id}
```

### 11.5 删除库位

```http
DELETE /api/locations/{id}
```

## 12. 入库管理

### 12.1 查询入库单列表

```http
GET /api/inbound-orders?page=1&pageSize=20&orderNo=IN202607130001&status=DRAFT&warehouseId=1
```

### 12.2 查询入库单详情

```http
GET /api/inbound-orders/{id}
```

### 12.3 创建入库单

```http
POST /api/inbound-orders
```

请求体：

```json
{
  "inboundType": "PURCHASE",
  "warehouseId": 1,
  "supplierId": 1,
  "remark": "采购入库",
  "items": [
    {
      "productId": 1,
      "areaId": 1,
      "locationId": 1,
      "quantity": 10,
      "unitPrice": 100
    }
  ],
  "fileIds": [1, 2]
}
```

### 12.4 编辑入库单

```http
PUT /api/inbound-orders/{id}
```

### 12.5 删除入库单

```http
DELETE /api/inbound-orders/{id}
```

### 12.6 提交入库单审核

```http
POST /api/inbound-orders/{id}/submit
```

### 12.7 审核入库单

```http
POST /api/inbound-orders/{id}/audit
```

请求体：

```json
{
  "approved": true,
  "auditRemark": "通过"
}
```

### 12.8 确认入库

```http
POST /api/inbound-orders/{id}/confirm
```

### 12.9 取消入库单

```http
POST /api/inbound-orders/{id}/cancel
```

## 13. 出库管理

### 13.1 查询出库单列表

```http
GET /api/outbound-orders?page=1&pageSize=20&orderNo=OUT202607130001&status=DRAFT&warehouseId=1
```

### 13.2 查询出库单详情

```http
GET /api/outbound-orders/{id}
```

### 13.3 创建出库单

```http
POST /api/outbound-orders
```

请求体：

```json
{
  "outboundType": "SALE",
  "warehouseId": 1,
  "customerId": 1,
  "remark": "销售出库",
  "items": [
    {
      "productId": 1,
      "areaId": 1,
      "locationId": 1,
      "quantity": 5,
      "unitPrice": 199
    }
  ],
  "fileIds": [3]
}
```

### 13.4 编辑出库单

```http
PUT /api/outbound-orders/{id}
```

### 13.5 删除出库单

```http
DELETE /api/outbound-orders/{id}
```

### 13.6 提交出库单审核

```http
POST /api/outbound-orders/{id}/submit
```

### 13.7 审核出库单

```http
POST /api/outbound-orders/{id}/audit
```

请求体：

```json
{
  "approved": true,
  "auditRemark": "通过"
}
```

### 13.8 确认出库

```http
POST /api/outbound-orders/{id}/confirm
```

### 13.9 取消出库单

```http
POST /api/outbound-orders/{id}/cancel
```

## 14. 库存管理

### 14.1 查询实时库存

```http
GET /api/stocks?page=1&pageSize=20&productId=1&warehouseId=1&locationId=1
```

### 14.2 查询库存详情

```http
GET /api/stocks/{id}
```

### 14.3 设置库存预警

```http
PUT /api/stocks/{id}/warning
```

请求体：

```json
{
  "warningMin": 10,
  "warningMax": 1000
}
```

### 14.4 查询库存预警列表

```http
GET /api/stocks/warnings?page=1&pageSize=20&warehouseId=1
```

### 14.5 导出库存

```http
GET /api/stocks/export?warehouseId=1
```

## 15. 库存流水

### 15.1 查询库存流水

```http
GET /api/stock-logs?page=1&pageSize=20&productId=1&warehouseId=1&changeType=INBOUND
```

### 15.2 查询库存流水详情

```http
GET /api/stock-logs/{id}
```

## 16. 调拨管理

### 16.1 查询调拨单列表

```http
GET /api/transfer-orders?page=1&pageSize=20&status=DRAFT
```

### 16.2 查询调拨单详情

```http
GET /api/transfer-orders/{id}
```

### 16.3 创建调拨单

```http
POST /api/transfer-orders
```

请求体：

```json
{
  "transferType": "LOCATION",
  "remark": "库位调拨",
  "items": [
    {
      "productId": 1,
      "fromWarehouseId": 1,
      "fromAreaId": 1,
      "fromLocationId": 1,
      "toWarehouseId": 1,
      "toAreaId": 2,
      "toLocationId": 5,
      "quantity": 3
    }
  ],
  "fileIds": [4]
}
```

### 16.4 编辑调拨单

```http
PUT /api/transfer-orders/{id}
```

### 16.5 提交调拨单审核

```http
POST /api/transfer-orders/{id}/submit
```

### 16.6 审核调拨单

```http
POST /api/transfer-orders/{id}/audit
```

### 16.7 执行调拨

```http
POST /api/transfer-orders/{id}/execute
```

### 16.8 取消调拨单

```http
POST /api/transfer-orders/{id}/cancel
```

## 17. 盘点管理

### 17.1 查询盘点单列表

```http
GET /api/inventory-checks?page=1&pageSize=20&warehouseId=1&status=DRAFT
```

### 17.2 查询盘点单详情

```http
GET /api/inventory-checks/{id}
```

### 17.3 创建盘点单

```http
POST /api/inventory-checks
```

请求体：

```json
{
  "warehouseId": 1,
  "areaId": 1,
  "locationId": 1,
  "remark": "月度盘点"
}
```

### 17.4 录入实盘数量

```http
PUT /api/inventory-checks/{id}/items
```

请求体：

```json
{
  "items": [
    {
      "itemId": 1,
      "actualQuantity": 98,
      "remark": "少 2 件"
    }
  ]
}
```

### 17.5 提交盘点单审核

```http
POST /api/inventory-checks/{id}/submit
```

### 17.6 审核盘点单

```http
POST /api/inventory-checks/{id}/audit
```

### 17.7 执行库存调整

```http
POST /api/inventory-checks/{id}/adjust
```

### 17.8 取消盘点单

```http
POST /api/inventory-checks/{id}/cancel
```

## 18. 文件管理

### 18.1 上传文件

```http
POST /api/files
```

请求类型：

```http
multipart/form-data
```

字段：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| file | file | 是 | 文件 |
| businessType | string | 否 | 业务类型 |
| businessId | number | 否 | 业务 ID |

### 18.2 查询文件详情

```http
GET /api/files/{id}
```

### 18.3 获取临时访问 URL

```http
GET /api/files/{id}/url
```

### 18.4 删除文件

```http
DELETE /api/files/{id}
```

## 19. 报表中心

### 19.1 当前库存报表

```http
GET /api/reports/stocks?warehouseId=1&productId=1
```

### 19.2 入库统计报表

```http
GET /api/reports/inbound?startDate=2026-07-01&endDate=2026-07-31&warehouseId=1
```

### 19.3 出库统计报表

```http
GET /api/reports/outbound?startDate=2026-07-01&endDate=2026-07-31&warehouseId=1
```

### 19.4 商品出入库明细报表

```http
GET /api/reports/product-stock-flow?productId=1&startDate=2026-07-01&endDate=2026-07-31
```

### 19.5 盘点差异报表

```http
GET /api/reports/inventory-differences?warehouseId=1&startDate=2026-07-01&endDate=2026-07-31
```

### 19.6 调拨统计报表

```http
GET /api/reports/transfers?warehouseId=1&startDate=2026-07-01&endDate=2026-07-31
```

## 20. 操作日志

### 20.1 查询操作日志

```http
GET /api/operation-logs?page=1&pageSize=20&moduleName=库存管理&operationType=确认出库
```

### 20.2 查询操作日志详情

```http
GET /api/operation-logs/{id}
```
