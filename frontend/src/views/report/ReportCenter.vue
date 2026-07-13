<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>报表中心 <span class="mock-badge">模拟</span></h2>
    </div>
    <div style="display:flex;gap:8px;margin-bottom:16px">
      <button class="btn" :class="activeTab==='stock'?'btn-primary':'btn-default'" @click="switchTab('stock')">库存报表</button>
      <button class="btn" :class="activeTab==='inbound'?'btn-primary':'btn-default'" @click="switchTab('inbound')">入库报表</button>
      <button class="btn" :class="activeTab==='outbound'?'btn-primary':'btn-default'" @click="switchTab('outbound')">出库报表</button>
    </div>
    <div class="table-card">
      <table v-if="activeTab==='stock'">
        <thead><tr><th>商品编码</th><th>商品名称</th><th>仓库</th><th>库存数量</th><th>可用数量</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.productCode }}</td><td>{{ item.productName }}</td><td>{{ item.warehouseName }}</td>
            <td>{{ item.quantity }}</td><td>{{ item.availableQty }}</td>
          </tr>
        </tbody>
      </table>
      <table v-if="activeTab==='inbound'">
        <thead><tr><th>单号</th><th>商品</th><th>数量</th><th>状态</th><th>时间</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.orderNo }}</td><td>{{ item.productName }}</td><td>{{ item.quantity }}</td>
            <td><span :class="'status-tag status-' + item.status.toLowerCase()">{{ item.status }}</span></td>
            <td>{{ item.createTime }}</td>
          </tr>
        </tbody>
      </table>
      <table v-if="activeTab==='outbound'">
        <thead><tr><th>单号</th><th>商品</th><th>数量</th><th>状态</th><th>时间</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.orderNo }}</td><td>{{ item.productName }}</td><td>{{ item.quantity }}</td>
            <td><span :class="'status-tag status-' + item.status.toLowerCase()">{{ item.status }}</span></td>
            <td>{{ item.createTime }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination"><span>共 {{ total }} 条</span></div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import * as api from '../../api/index.js'
const activeTab = ref('stock')
const list = ref([])
const total = ref(0)
async function switchTab(tab) {
  activeTab.value = tab
  if (tab === 'stock') { const r = await api.getReportStocks({ page:1, pageSize:20 }); list.value = r.data.records; total.value = r.data.total }
  else if (tab === 'inbound') { const r = await api.getInboundOrders({ page:1, pageSize:20 }); list.value = r.data.records; total.value = r.data.total }
  else { const r = await api.getOutboundOrders({ page:1, pageSize:20 }); list.value = r.data.records; total.value = r.data.total }
}
switchTab('stock')
</script>