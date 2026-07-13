import client from '../client.js'

// ⚠️ 真实API服务 - 连接后端 Spring Boot 服务
// 后端启动后设置 VITE_USE_MOCK=false 即可切换

// ==================== 认证 ====================
export async function login(username, password) {
  return client.post('/auth/login', { username, password })
}
export async function logout() {
  return client.post('/auth/logout')
}
export async function getMe() {
  return client.get('/auth/me')
}
export async function changePassword(data) {
  return client.put('/auth/password', data)
}

// ==================== 用户管理 ====================
export async function getUsers(params = {}) { return client.get('/users', { params }) }
export async function getUserById(id) { return client.get(`/users/${id}`) }
export async function createUser(data) { return client.post('/users', data) }
export async function updateUser(id, data) { return client.put(`/users/${id}`, data) }
export async function deleteUser(id) { return client.delete(`/users/${id}`) }
export async function updateUserStatus(id, status) { return client.patch(`/users/${id}/status`, { status }) }
export async function resetUserPassword(id) { return client.put(`/users/${id}/password/reset`) }

// ==================== 角色管理 ====================
export async function getRoles(params = {}) { return client.get('/roles', { params }) }
export async function createRole(data) { return client.post('/roles', data) }
export async function updateRole(id, data) { return client.put(`/roles/${id}`, data) }
export async function deleteRole(id) { return client.delete(`/roles/${id}`) }
export async function getPermissionTree() { return client.get('/permissions/tree') }
export async function assignPermissions(roleId, permissionIds) { return client.put(`/roles/${roleId}/permissions`, { permissionIds }) }

// ==================== 商品管理 ====================
export async function getProducts(params = {}) { return client.get('/products', { params }) }
export async function getProductById(id) { return client.get(`/products/${id}`) }
export async function createProduct(data) { return client.post('/products', data) }
export async function updateProduct(id, data) { return client.put(`/products/${id}`, data) }
export async function deleteProduct(id) { return client.delete(`/products/${id}`) }
export async function updateProductStatus(id, status) { return client.patch(`/products/${id}/status`, { status }) }

// ==================== 商品分类 ====================
export async function getCategories() { return client.get('/product-categories/tree') }
export async function createCategory(data) { return client.post('/product-categories', data) }
export async function updateCategory(id, data) { return client.put(`/product-categories/${id}`, data) }
export async function deleteCategory(id) { return client.delete(`/product-categories/${id}`) }

// ==================== 供应商 ====================
export async function getSuppliers(params = {}) { return client.get('/suppliers', { params }) }
export async function createSupplier(data) { return client.post('/suppliers', data) }
export async function updateSupplier(id, data) { return client.put(`/suppliers/${id}`, data) }
export async function deleteSupplier(id) { return client.delete(`/suppliers/${id}`) }

// ==================== 客户 ====================
export async function getCustomers(params = {}) { return client.get('/customers', { params }) }
export async function createCustomer(data) { return client.post('/customers', data) }
export async function updateCustomer(id, data) { return client.put(`/customers/${id}`, data) }
export async function deleteCustomer(id) { return client.delete(`/customers/${id}`) }

// ==================== 仓库 ====================
export async function getWarehouses(params = {}) { return client.get('/warehouses', { params }) }
export async function getWarehouseById(id) { return client.get(`/warehouses/${id}`) }
export async function createWarehouse(data) { return client.post('/warehouses', data) }
export async function updateWarehouse(id, data) { return client.put(`/warehouses/${id}`, data) }
export async function deleteWarehouse(id) { return client.delete(`/warehouses/${id}`) }
export async function updateWarehouseStatus(id, status) { return client.patch(`/warehouses/${id}/status`, { status }) }

// ==================== 库区 ====================
export async function getAreas(params = {}) { return client.get('/warehouse-areas', { params }) }
export async function createArea(data) { return client.post('/warehouse-areas', data) }
export async function updateArea(id, data) { return client.put(`/warehouse-areas/${id}`, data) }
export async function deleteArea(id) { return client.delete(`/warehouse-areas/${id}`) }

// ==================== 库位 ====================
export async function getLocations(params = {}) { return client.get('/locations', { params }) }
export async function createLocation(data) { return client.post('/locations', data) }
export async function updateLocation(id, data) { return client.put(`/locations/${id}`, data) }
export async function deleteLocation(id) { return client.delete(`/locations/${id}`) }

// ==================== 入库管理 ====================
export async function getInboundOrders(params = {}) { return client.get('/inbound-orders', { params }) }
export async function getInboundOrderById(id) { return client.get(`/inbound-orders/${id}`) }
export async function createInboundOrder(data) { return client.post('/inbound-orders', data) }
export async function updateInboundOrder(id, data) { return client.put(`/inbound-orders/${id}`, data) }
export async function submitInboundOrder(id) { return client.post(`/inbound-orders/${id}/submit`) }
export async function auditInboundOrder(id, status, auditRemark) { return client.post(`/inbound-orders/${id}/audit`, { status, auditRemark }) }
export async function confirmInboundOrder(id) { return client.post(`/inbound-orders/${id}/confirm`) }
export async function cancelInboundOrder(id) { return client.post(`/inbound-orders/${id}/cancel`) }

// ==================== 出库管理 ====================
export async function getOutboundOrders(params = {}) { return client.get('/outbound-orders', { params }) }
export async function getOutboundOrderById(id) { return client.get(`/outbound-orders/${id}`) }
export async function createOutboundOrder(data) { return client.post('/outbound-orders', data) }
export async function updateOutboundOrder(id, data) { return client.put(`/outbound-orders/${id}`, data) }
export async function submitOutboundOrder(id) { return client.post(`/outbound-orders/${id}/submit`) }
export async function auditOutboundOrder(id, status, auditRemark) { return client.post(`/outbound-orders/${id}/audit`, { status, auditRemark }) }
export async function confirmOutboundOrder(id) { return client.post(`/outbound-orders/${id}/confirm`) }
export async function cancelOutboundOrder(id) { return client.post(`/outbound-orders/${id}/cancel`) }

// ==================== 库存 ====================
export async function getStocks(params = {}) { return client.get('/stocks', { params }) }
export async function getStockById(id) { return client.get(`/stocks/${id}`) }
export async function updateStockWarning(id, data) { return client.put(`/stocks/${id}/warning`, data) }
export async function getStockWarnings(params = {}) { return client.get('/stocks/warnings', { params }) }

// ==================== 库存流水 ====================
export async function getStockLogs(params = {}) { return client.get('/stock-logs', { params }) }
export async function getStockLogById(id) { return client.get(`/stock-logs/${id}`) }

// ==================== 调拨管理 ====================
export async function getTransferOrders(params = {}) { return client.get('/transfer-orders', { params }) }
export async function getTransferOrderById(id) { return client.get(`/transfer-orders/${id}`) }
export async function createTransferOrder(data) { return client.post('/transfer-orders', data) }
export async function updateTransferOrder(id, data) { return client.put(`/transfer-orders/${id}`, data) }
export async function submitTransferOrder(id) { return client.post(`/transfer-orders/${id}/submit`) }
export async function auditTransferOrder(id, status, auditRemark) { return client.post(`/transfer-orders/${id}/audit`, { status, auditRemark }) }
export async function executeTransferOrder(id) { return client.post(`/transfer-orders/${id}/execute`) }
export async function cancelTransferOrder(id) { return client.post(`/transfer-orders/${id}/cancel`) }

// ==================== 盘点管理 ====================
export async function getInventoryChecks(params = {}) { return client.get('/inventory-checks', { params }) }
export async function getInventoryCheckById(id) { return client.get(`/inventory-checks/${id}`) }
export async function createInventoryCheck(data) { return client.post('/inventory-checks', data) }
export async function updateInventoryItems(id, items) { return client.put(`/inventory-checks/${id}/items`, { items }) }
export async function submitInventoryCheck(id) { return client.post(`/inventory-checks/${id}/submit`) }
export async function auditInventoryCheck(id, status, auditRemark) { return client.post(`/inventory-checks/${id}/audit`, { status, auditRemark }) }
export async function adjustInventoryCheck(id) { return client.post(`/inventory-checks/${id}/adjust`) }
export async function cancelInventoryCheck(id) { return client.post(`/inventory-checks/${id}/cancel`) }

// ==================== 报表 ====================
export async function getReportStocks(params = {}) { return client.get('/reports/stocks', { params }) }
export async function getReportInbound(params = {}) { return client.get('/reports/inbound', { params }) }
export async function getReportOutbound(params = {}) { return client.get('/reports/outbound', { params }) }
export async function getReportProductFlow(params = {}) { return client.get('/reports/product-stock-flow', { params }) }
export async function getReportInventoryDiff(params = {}) { return client.get('/reports/inventory-differences', { params }) }
export async function getReportTransfers(params = {}) { return client.get('/reports/transfers', { params }) }

// ==================== 操作日志 ====================
export async function getOperationLogs(params = {}) { return client.get('/operation-logs', { params }) }
export async function getOperationLogById(id) { return client.get(`/operation-logs/${id}`) }