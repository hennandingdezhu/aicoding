import { createRouter, createWebHistory } from "vue-router"

const routes = [
  { path: "/login", name: "Login", component: () => import("../views/Login.vue") },
  { path: "/", name: "Dashboard", component: () => import("../views/Dashboard.vue"), meta: { title: "首页" } },
  { path: "/products", name: "ProductList", component: () => import("../views/product/ProductList.vue"), meta: { title: "商品管理" } },
  { path: "/products/create", name: "ProductCreate", component: () => import("../views/product/ProductForm.vue"), meta: { title: "新增商品" } },
  { path: "/products/:id/edit", name: "ProductEdit", component: () => import("../views/product/ProductForm.vue"), meta: { title: "编辑商品" } },
  { path: "/categories", name: "CategoryList", component: () => import("../views/product/CategoryList.vue"), meta: { title: "商品分类" } },
  { path: "/suppliers", name: "SupplierList", component: () => import("../views/product/SupplierList.vue"), meta: { title: "供应商管理" } },
  { path: "/customers", name: "CustomerList", component: () => import("../views/product/CustomerList.vue"), meta: { title: "客户管理" } },
  { path: "/warehouses", name: "WarehouseList", component: () => import("../views/warehouse/WarehouseList.vue"), meta: { title: "仓库管理" } },
  { path: "/areas", name: "AreaList", component: () => import("../views/warehouse/AreaList.vue"), meta: { title: "库区管理" } },
  { path: "/locations", name: "LocationList", component: () => import("../views/warehouse/LocationList.vue"), meta: { title: "库位管理" } },
  { path: "/inbound", name: "InboundList", component: () => import("../views/inbound/InboundList.vue"), meta: { title: "入库管理" } },
  { path: "/inbound/create", name: "InboundCreate", component: () => import("../views/inbound/InboundForm.vue"), meta: { title: "创建入库单" } },
  { path: "/inbound/:id", name: "InboundDetail", component: () => import("../views/inbound/InboundForm.vue"), meta: { title: "入库单详情" } },
  { path: "/outbound", name: "OutboundList", component: () => import("../views/outbound/OutboundList.vue"), meta: { title: "出库管理" } },
  { path: "/outbound/create", name: "OutboundCreate", component: () => import("../views/outbound/OutboundForm.vue"), meta: { title: "创建出库单" } },
  { path: "/outbound/:id", name: "OutboundDetail", component: () => import("../views/outbound/OutboundForm.vue"), meta: { title: "出库单详情" } },
  { path: "/stocks", name: "StockList", component: () => import("../views/stock/StockList.vue"), meta: { title: "实时库存" } },
  { path: "/stock-logs", name: "StockLogList", component: () => import("../views/stock/StockLogList.vue"), meta: { title: "库存流水" } },
  { path: "/stock-warnings", name: "StockWarning", component: () => import("../views/stock/StockWarning.vue"), meta: { title: "库存预警" } },
  { path: "/transfers", name: "TransferList", component: () => import("../views/transfer/TransferList.vue"), meta: { title: "调拨管理" } },
  { path: "/transfers/create", name: "TransferCreate", component: () => import("../views/transfer/TransferForm.vue"), meta: { title: "创建调拨单" } },
  { path: "/transfers/:id", name: "TransferDetail", component: () => import("../views/transfer/TransferForm.vue"), meta: { title: "调拨单详情" } },
  { path: "/inventory", name: "InventoryList", component: () => import("../views/inventory/InventoryList.vue"), meta: { title: "盘点管理" } },
  { path: "/inventory/create", name: "InventoryCreate", component: () => import("../views/inventory/InventoryForm.vue"), meta: { title: "创建盘点单" } },
  { path: "/inventory/:id", name: "InventoryDetail", component: () => import("../views/inventory/InventoryForm.vue"), meta: { title: "盘点单详情" } },
  { path: "/reports", name: "ReportCenter", component: () => import("../views/report/ReportCenter.vue"), meta: { title: "报表中心" } },
  { path: "/users", name: "UserList", component: () => import("../views/system/UserList.vue"), meta: { title: "用户管理" } },
  { path: "/roles", name: "RoleList", component: () => import("../views/system/RoleList.vue"), meta: { title: "角色管理" } },
  { path: "/logs", name: "OperationLogList", component: () => import("../views/system/OperationLogList.vue"), meta: { title: "操作日志" } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token")
  if (to.path !== "/login" && !token) {
    next("/login")
  } else {
    next()
  }
})

export default router