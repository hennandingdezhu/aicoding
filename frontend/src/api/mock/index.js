import mockData from "./data.js"

// ⚠️ 模拟API服务 - 所有数据均为模拟数据，不连接真实后端
const delay = (ms = 200) => new Promise((r) => setTimeout(r, ms))

function paginate(list, page = 1, pageSize = 20) {
  const start = (page - 1) * pageSize
  return { records: list.slice(start, start + pageSize), total: list.length, page, pageSize }
}

function ok(data) {
  return { code: 200, message: "success", data }
}

// ==================== 认证 ====================
export async function login(username, password) {
  await delay()
  if (username === "admin" && password === "admin123") {
    const user = mockData.users[0]
    return ok({ token: "mock-token-admin-2026", user })
  }
  return { code: 401, message: "用户名或密码错误", data: null }
}

export async function logout() {
  await delay()
  return ok(null)
}

export async function getMe() {
  await delay()
  return ok(mockData.users[0])
}

export async function changePassword(data) {
  await delay()
  return ok(null)
}

// ==================== 用户管理 ====================
export async function getUsers(params = {}) {
  await delay()
  let list = [...mockData.users]
  if (params.username) list = list.filter((u) => u.username.includes(params.username))
  if (params.realName) list = list.filter((u) => u.realName.includes(params.realName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createUser(data) {
  await delay()
  return ok(null)
}

export async function updateUser(data) {
  await delay()
  return ok(null)
}

export async function deleteUser(id) {
  await delay()
  return ok(null)
}

export async function updateUserStatus(id, status) {
  await delay()
  return ok(null)
}

export async function resetPassword(id) {
  await delay()
  return ok(null)
}

// ==================== 角色管理 ====================
export async function getRoles(params = {}) {
  await delay()
  let list = [...mockData.roles]
  if (params.roleName) list = list.filter((r) => r.roleName.includes(params.roleName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createRole(data) {
  await delay()
  return ok(null)
}

export async function updateRole(data) {
  await delay()
  return ok(null)
}

export async function deleteRole(id) {
  await delay()
  return ok(null)
}

export async function assignPermissions(roleId, permissionIds) {
  await delay()
  return ok(null)
}

export async function getPermissionTree() {
  await delay()
  return ok(mockData.permissionTree)
}

// ==================== 商品管理 ====================
export async function getProducts(params = {}) {
  await delay()
  let list = [...mockData.products]
  if (params.productName) list = list.filter((p) => p.productName.includes(params.productName))
  if (params.productCode) list = list.filter((p) => p.productCode.includes(params.productCode))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createProduct(data) {
  await delay()
  return ok(null)
}

export async function updateProduct(data) {
  await delay()
  return ok(null)
}

export async function deleteProduct(id) {
  await delay()
  return ok(null)
}

export async function updateProductStatus(id, status) {
  await delay()
  return ok(null)
}

// ==================== 商品分类 ====================
export async function getCategories(params = {}) {
  await delay()
  let list = [...mockData.categories]
  if (params.categoryName) list = list.filter((c) => c.categoryName.includes(params.categoryName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createCategory(data) {
  await delay()
  return ok(null)
}

export async function updateCategory(data) {
  await delay()
  return ok(null)
}

export async function deleteCategory(id) {
  await delay()
  return ok(null)
}

// ==================== 供应商 ====================
export async function getSuppliers(params = {}) {
  await delay()
  let list = [...mockData.suppliers]
  if (params.supplierName) list = list.filter((s) => s.supplierName.includes(params.supplierName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createSupplier(data) {
  await delay()
  return ok(null)
}

export async function updateSupplier(data) {
  await delay()
  return ok(null)
}

export async function deleteSupplier(id) {
  await delay()
  return ok(null)
}

// ==================== 客户 ====================
export async function getCustomers(params = {}) {
  await delay()
  let list = [...mockData.customers]
  if (params.customerName) list = list.filter((c) => c.customerName.includes(params.customerName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createCustomer(data) {
  await delay()
  return ok(null)
}

export async function updateCustomer(data) {
  await delay()
  return ok(null)
}

export async function deleteCustomer(id) {
  await delay()
  return ok(null)
}

// ==================== 仓库 ====================
export async function getWarehouses(params = {}) {
  await delay()
  let list = [...mockData.warehouses]
  if (params.warehouseName) list = list.filter((w) => w.warehouseName.includes(params.warehouseName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createWarehouse(data) {
  await delay()
  return ok(null)
}

export async function updateWarehouse(data) {
  await delay()
  return ok(null)
}

export async function deleteWarehouse(id) {
  await delay()
  return ok(null)
}

export async function updateWarehouseStatus(id, status) {
  await delay()
  return ok(null)
}

// ==================== 库区 ====================
export async function getAreas(params = {}) {
  await delay()
  let list = [...mockData.areas]
  if (params.areaName) list = list.filter((a) => a.areaName.includes(params.areaName))
  if (params.warehouseId) list = list.filter((a) => a.warehouseId === params.warehouseId)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createArea(data) {
  await delay()
  return ok(null)
}

export async function updateArea(data) {
  await delay()
  return ok(null)
}

export async function deleteArea(id) {
  await delay()
  return ok(null)
}

// ==================== 库位 ====================
export async function getLocations(params = {}) {
  await delay()
  let list = [...mockData.locations]
  if (params.locationName) list = list.filter((l) => l.locationName.includes(params.locationName))
  if (params.warehouseId) list = list.filter((l) => l.warehouseId === params.warehouseId)
  if (params.areaId) list = list.filter((l) => l.areaId === params.areaId)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createLocation(data) {
  await delay()
  return ok(null)
}

export async function updateLocation(data) {
  await delay()
  return ok(null)
}

export async function deleteLocation(id) {
  await delay()
  return ok(null)
}

// ==================== 入库单 ====================
export async function getInboundOrders(params = {}) {
  await delay()
  let list = [...mockData.inboundOrders]
  if (params.orderNo) list = list.filter((o) => o.orderNo.includes(params.orderNo))
  if (params.status) list = list.filter((o) => o.status === params.status)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createInboundOrder(data) {
  await delay()
  return ok(null)
}

export async function updateInboundOrder(data) {
  await delay()
  return ok(null)
}

export async function submitInboundOrder(id) {
  await delay()
  return ok(null)
}

export async function auditInboundOrder(id) {
  await delay()
  return ok(null)
}

export async function confirmInboundOrder(id) {
  await delay()
  return ok(null)
}

export async function cancelInboundOrder(id) {
  await delay()
  return ok(null)
}

// ==================== 出库单 ====================
export async function getOutboundOrders(params = {}) {
  await delay()
  let list = [...mockData.outboundOrders]
  if (params.orderNo) list = list.filter((o) => o.orderNo.includes(params.orderNo))
  if (params.status) list = list.filter((o) => o.status === params.status)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createOutboundOrder(data) {
  await delay()
  return ok(null)
}

export async function updateOutboundOrder(data) {
  await delay()
  return ok(null)
}

export async function submitOutboundOrder(id) {
  await delay()
  return ok(null)
}

export async function auditOutboundOrder(id) {
  await delay()
  return ok(null)
}

export async function confirmOutboundOrder(id) {
  await delay()
  return ok(null)
}

export async function cancelOutboundOrder(id) {
  await delay()
  return ok(null)
}

// ==================== 库存 ====================
export async function getStocks(params = {}) {
  await delay()
  let list = [...mockData.stocks]
  if (params.productName) list = list.filter((s) => s.productName.includes(params.productName))
  if (params.warehouseId) list = list.filter((s) => s.warehouseId === params.warehouseId)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function getStockById(id) {
  await delay()
  return ok(mockData.stocks.find((s) => s.id === id) || null)
}

export async function updateStockWarning(id, data) {
  await delay()
  return ok(null)
}

export async function getStockWarnings(params = {}) {
  await delay()
  const list = mockData.stocks.filter(
    (s) => s.availableQuantity <= s.warningMin || s.availableQuantity >= s.warningMax
  )
  return ok(paginate(list, params.page, params.pageSize))
}

// ==================== 库存流水 ====================
export async function getStockLogs(params = {}) {
  await delay()
  let list = [...mockData.stockLogs]
  if (params.productName) list = list.filter((l) => l.productName.includes(params.productName))
  if (params.changeType) list = list.filter((l) => l.changeType === params.changeType)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function getStockLogById(id) {
  await delay()
  return ok(mockData.stockLogs.find((l) => l.id === id) || null)
}

// ==================== 调拨单 ====================
export async function getTransferOrders(params = {}) {
  await delay()
  let list = [...mockData.transferOrders]
  if (params.orderNo) list = list.filter((o) => o.orderNo.includes(params.orderNo))
  if (params.status) list = list.filter((o) => o.status === params.status)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createTransferOrder(data) {
  await delay()
  return ok(null)
}

export async function updateTransferOrder(data) {
  await delay()
  return ok(null)
}

export async function submitTransferOrder(id) {
  await delay()
  return ok(null)
}

export async function auditTransferOrder(id) {
  await delay()
  return ok(null)
}

export async function executeTransferOrder(id) {
  await delay()
  return ok(null)
}

export async function cancelTransferOrder(id) {
  await delay()
  return ok(null)
}

// ==================== 盘点单 ====================
export async function getInventoryChecks(params = {}) {
  await delay()
  let list = [...mockData.inventoryChecks]
  if (params.orderNo) list = list.filter((o) => o.orderNo.includes(params.orderNo))
  if (params.status) list = list.filter((o) => o.status === params.status)
  return ok(paginate(list, params.page, params.pageSize))
}

export async function createInventoryCheck(data) {
  await delay()
  return ok(null)
}

export async function updateInventoryItems(id, items) {
  await delay()
  return ok(null)
}

export async function submitInventoryCheck(id) {
  await delay()
  return ok(null)
}

export async function auditInventoryCheck(id) {
  await delay()
  return ok(null)
}

export async function adjustInventoryCheck(id) {
  await delay()
  return ok(null)
}

export async function cancelInventoryCheck(id) {
  await delay()
  return ok(null)
}

// ==================== 操作日志 ====================
export async function getOperationLogs(params = {}) {
  await delay()
  let list = [...mockData.operationLogs]
  if (params.operatorName) list = list.filter((l) => l.operatorName.includes(params.operatorName))
  if (params.moduleName) list = list.filter((l) => l.moduleName.includes(params.moduleName))
  return ok(paginate(list, params.page, params.pageSize))
}

export async function getOperationLogById(id) {
  await delay()
  return ok(mockData.operationLogs.find((l) => l.id === id) || null)
}

// ==================== 报表 ====================
export async function getReportStocks(params = {}) {
  await delay()
  return ok(mockData.stocks)
}

export async function getReportInbound(params = {}) {
  await delay()
  return ok(mockData.inboundOrders)
}

export async function getReportOutbound(params = {}) {
  await delay()
  return ok(mockData.outboundOrders)
}

export async function getReportProductFlow(params = {}) {
  await delay()
  return ok(mockData.stockLogs)
}

export async function getReportInventoryDiff(params = {}) {
  await delay()
  return ok(mockData.inventoryChecks)
}

export async function getReportTransfers(params = {}) {
  await delay()
  return ok(mockData.transferOrders)
}