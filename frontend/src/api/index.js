// API 切换层：根据环境变量 VITE_USE_MOCK 决定使用 mock 或真实后端
// 默认使用 mock（npm run dev），设置 VITE_USE_MOCK=false 连接后端

const useMock = import.meta.env.VITE_USE_MOCK !== 'false'

let api
if (useMock) {
  api = await import('./mock/index.js')
} else {
  api = await import('./real/index.js')
}

export const {
  login, logout, getMe, changePassword,
  getUsers, getUserById, createUser, updateUser, deleteUser, updateUserStatus, resetUserPassword,
  getRoles, createRole, updateRole, deleteRole, getPermissionTree, assignPermissions,
  getProducts, getProductById, createProduct, updateProduct, deleteProduct, updateProductStatus,
  getCategories, createCategory, updateCategory, deleteCategory,
  getSuppliers, createSupplier, updateSupplier, deleteSupplier,
  getCustomers, createCustomer, updateCustomer, deleteCustomer,
  getWarehouses, getWarehouseById, createWarehouse, updateWarehouse, deleteWarehouse, updateWarehouseStatus,
  getAreas, createArea, updateArea, deleteArea,
  getLocations, createLocation, updateLocation, deleteLocation,
  getInboundOrders, getInboundOrderById, createInboundOrder, updateInboundOrder,
  submitInboundOrder, auditInboundOrder, confirmInboundOrder, cancelInboundOrder,
  getOutboundOrders, getOutboundOrderById, createOutboundOrder, updateOutboundOrder,
  submitOutboundOrder, auditOutboundOrder, confirmOutboundOrder, cancelOutboundOrder,
  getStocks, getStockById, updateStockWarning, getStockWarnings,
  getStockLogs, getStockLogById,
  getTransferOrders, getTransferOrderById, createTransferOrder, updateTransferOrder,
  submitTransferOrder, auditTransferOrder, executeTransferOrder, cancelTransferOrder,
  getInventoryChecks, getInventoryCheckById, createInventoryCheck, updateInventoryItems,
  submitInventoryCheck, auditInventoryCheck, adjustInventoryCheck, cancelInventoryCheck,
  getReportStocks, getReportInbound, getReportOutbound, getReportProductFlow, getReportInventoryDiff, getReportTransfers,
  getOperationLogs, getOperationLogById
} = api