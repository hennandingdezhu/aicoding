// ⚠️ 模拟数据库 - 仅用于前端开发演示，不连接真实数据库
export default {
  users: [
    { id: 1, username: "admin", realName: "管理员", phone: "13800000001", email: "admin@demo.com", status: 1, roleNames: "系统管理员,仓库主管", warehouseNames: "上海总仓", lastLoginTime: "2026-07-13 08:30:00", createdAt: "2026-01-01" },
    { id: 2, username: "zhangsan", realName: "张三", phone: "13800000002", email: "zhangsan@demo.com", status: 1, roleNames: "仓管员", warehouseNames: "上海总仓", lastLoginTime: "2026-07-13 09:15:00", createdAt: "2026-03-15" }
  ],
  roles: [
    { id: 1, roleCode: "admin", roleName: "系统管理员", description: "管理系统所有功能", status: 1, createdAt: "2026-01-01" },
    { id: 2, roleCode: "warehouse_manager", roleName: "仓库主管", description: "负责仓库审核和管理", status: 1, createdAt: "2026-01-01" }
  ],
  products: [
    { id: 1, productCode: "SKU001", productName: "机械键盘", categoryName: "电脑外设", specification: "87键 红轴", unit: "个", brand: "Demo", costPrice: 100.00, salePrice: 199.00, status: 1, createdAt: "2026-01-01" },
    { id: 2, productCode: "SKU002", productName: "无线鼠标", categoryName: "电脑外设", specification: "蓝牙5.0", unit: "个", brand: "Demo", costPrice: 30.00, salePrice: 79.00, status: 1, createdAt: "2026-01-01" }
  ],
  categories: [
    { id: 1, categoryCode: "CAT001", categoryName: "电脑外设", parentId: null, sortOrder: 1, status: 1, children: [] },
    { id: 2, categoryCode: "CAT002", categoryName: "电子元器件", parentId: null, sortOrder: 2, status: 1, children: [] }
  ],
  suppliers: [
    { id: 1, supplierCode: "SUP001", supplierName: "深圳电子有限公司", contactPerson: "李四", contactPhone: "13900000001", address: "深圳市宝安区", status: 1, createdAt: "2026-01-01" },
    { id: 2, supplierCode: "SUP002", supplierName: "上海科技股份有限公司", contactPerson: "王五", contactPhone: "13900000002", address: "上海市浦东新区", status: 1, createdAt: "2026-01-01" }
  ],
  customers: [
    { id: 1, customerCode: "CUS001", customerName: "北京贸易公司", contactPerson: "赵六", contactPhone: "13700000001", address: "北京市朝阳区", status: 1, createdAt: "2026-01-01" },
    { id: 2, customerCode: "CUS002", customerName: "广州商贸有限公司", contactPerson: "钱七", contactPhone: "13700000002", address: "广州市天河区", status: 1, createdAt: "2026-01-01" }
  ],
  warehouses: [
    { id: 1, warehouseCode: "WH001", warehouseName: "上海总仓", address: "上海市松江区", managerName: "张三", managerPhone: "13800000002", status: 1, createdAt: "2026-01-01" },
    { id: 2, warehouseCode: "WH002", warehouseName: "深圳分仓", address: "深圳市龙华区", managerName: "李四", managerPhone: "13900000001", status: 1, createdAt: "2026-03-01" }
  ],
  areas: [
    { id: 1, warehouseId: 1, warehouseName: "上海总仓", areaCode: "A-01", areaName: "A区-电子元器件", status: 1, createdAt: "2026-01-01" },
    { id: 2, warehouseId: 1, warehouseName: "上海总仓", areaCode: "B-01", areaName: "B区-成品区", status: 1, createdAt: "2026-01-01" }
  ],
  locations: [
    { id: 1, warehouseId: 1, warehouseName: "上海总仓", areaId: 1, areaName: "A区-电子元器件", locationCode: "A-01-01", locationName: "A-01-01货架", capacity: 500, status: 1, createdAt: "2026-01-01" },
    { id: 2, warehouseId: 1, warehouseName: "上海总仓", areaId: 2, areaName: "B区-成品区", locationCode: "B-01-01", locationName: "B-01-01货架", capacity: 300, status: 1, createdAt: "2026-01-01" }
  ],
  inboundOrders: [
    { id: 1, orderNo: "IN-20260713-000001", inboundType: "PURCHASE", warehouseId: 1, warehouseName: "上海总仓", supplierId: 1, supplierName: "深圳电子有限公司", status: "CONFIRMED", createdAt: "2026-07-13", createdBy: "admin", items: [
      { id: 1, productId: 1, productName: "机械键盘", areaId: 1, areaName: "A区", locationId: 1, locationName: "A-01-01", quantity: 100, unitPrice: 100.00 }
    ]},
    { id: 2, orderNo: "IN-20260713-000002", inboundType: "PURCHASE", warehouseId: 1, warehouseName: "上海总仓", supplierId: 2, supplierName: "上海科技股份有限公司", status: "PENDING", createdAt: "2026-07-13", createdBy: "zhangsan", items: [
      { id: 2, productId: 2, productName: "无线鼠标", areaId: 2, areaName: "B区", locationId: 2, locationName: "B-01-01", quantity: 50, unitPrice: 30.00 }
    ]}
  ],
  outboundOrders: [
    { id: 1, orderNo: "OUT-20260713-000001", outboundType: "SALE", warehouseId: 1, warehouseName: "上海总仓", customerId: 1, customerName: "北京贸易公司", status: "CONFIRMED", createdAt: "2026-07-13", createdBy: "admin", items: [
      { id: 1, productId: 1, productName: "机械键盘", areaId: 2, areaName: "B区", locationId: 2, locationName: "B-01-01", quantity: 20, unitPrice: 199.00 }
    ]},
    { id: 2, orderNo: "OUT-20260713-000002", outboundType: "SALE", warehouseId: 1, warehouseName: "上海总仓", customerId: 2, customerName: "广州商贸有限公司", status: "DRAFT", createdAt: "2026-07-13", createdBy: "zhangsan", items: [
      { id: 2, productId: 2, productName: "无线鼠标", areaId: 2, areaName: "B区", locationId: 2, locationName: "B-01-01", quantity: 10, unitPrice: 79.00 }
    ]}
  ],
  stocks: [
    { id: 1, productId: 1, productName: "机械键盘", warehouseId: 1, warehouseName: "上海总仓", areaId: 2, areaName: "B区", locationId: 2, locationName: "B-01-01", totalQuantity: 80, availableQuantity: 70, lockedQuantity: 10, warningMin: 5, warningMax: 200 },
    { id: 2, productId: 2, productName: "无线鼠标", warehouseId: 1, warehouseName: "上海总仓", areaId: 2, areaName: "B区", locationId: 2, locationName: "B-01-01", totalQuantity: 50, availableQuantity: 45, lockedQuantity: 5, warningMin: 10, warningMax: 300 }
  ],
  stockLogs: [
    { id: 1, logNo: "SL-001", productName: "机械键盘", warehouseName: "上海总仓", changeType: "INBOUND", beforeQuantity: 0, changeQuantity: 100, afterQuantity: 100, businessNo: "IN-20260713-000001", operatedBy: "admin", operatedAt: "2026-07-13 08:30:00" },
    { id: 2, logNo: "SL-002", productName: "机械键盘", warehouseName: "上海总仓", changeType: "OUTBOUND", beforeQuantity: 100, changeQuantity: -20, afterQuantity: 80, businessNo: "OUT-20260713-000001", operatedBy: "admin", operatedAt: "2026-07-13 14:00:00" }
  ],
  transferOrders: [
    { id: 1, orderNo: "TR-20260713-000001", transferType: "LOCATION", status: "APPROVED", remark: "库位调整", createdAt: "2026-07-13", createdBy: "admin", items: [
      { id: 1, productId: 1, productName: "机械键盘", fromWarehouseId: 1, fromAreaId: 1, fromLocationId: 1, toWarehouseId: 1, toAreaId: 2, toLocationId: 2, quantity: 10 }
    ]}
  ],
  inventoryChecks: [
    { id: 1, orderNo: "IC-20260713-000001", warehouseId: 1, warehouseName: "上海总仓", status: "DRAFT", remark: "月度盘点", createdAt: "2026-07-13", createdBy: "admin", items: [
      { id: 1, productId: 1, productName: "机械键盘", bookQuantity: 80, actualQuantity: 78, differenceQuantity: -2 }
    ]}
  ],
  operationLogs: [
    { id: 1, operatorName: "admin", moduleName: "登录认证", operationType: "用户登录", operationContent: "用户 admin 登录系统", requestUri: "/api/auth/login", ipAddress: "192.168.1.100", createdAt: "2026-07-13 08:00:00" },
    { id: 2, operatorName: "admin", moduleName: "入库管理", operationType: "确认入库", operationContent: "确认入库单 IN-20260713-000001", requestUri: "/api/inbound-orders/1/confirm", ipAddress: "192.168.1.100", createdAt: "2026-07-13 08:30:00" }
  ],
  permissionTree: [
    { id: 1, permissionCode: "dashboard", permissionName: "首页", permissionType: "MENU", children: [] },
    { id: 2, permissionCode: "base_data", permissionName: "基础资料", permissionType: "MENU", children: [
      { id: 3, permissionCode: "product", permissionName: "商品管理", permissionType: "MENU", children: [] },
      { id: 4, permissionCode: "supplier", permissionName: "供应商管理", permissionType: "MENU", children: [] }
    ]}
  ]
}