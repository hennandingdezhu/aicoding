<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>库存日志 <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索商品..." />
        <select v-model="changeType"><option value="">全部类型</option><option value="IN">入库</option><option value="OUT">出库</option><option value="TRANSFER">调拨</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>商品</th><th>仓库</th><th>变更类型</th><th>变更数量</th><th>变更前</th><th>变更后</th><th>时间</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.productName }}</td>
            <td>{{ item.warehouseName }}</td>
            <td><span :class="'status-tag ' + (item.changeType==='IN'?'status-confirmed':'status-rejected')">{{ item.changeType==='IN'?'入库':item.changeType==='OUT'?'出库':'调拨' }}</span></td>
            <td>{{ item.changeQty }}</td>
            <td>{{ item.beforeQty }}</td>
            <td>{{ item.afterQty }}</td>
            <td>{{ item.createTime }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination"><span>共 {{ total }} 条</span></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import * as api from '../../api/index.js'
const list = ref([])
const total = ref(0)
const keyword = ref('')
const changeType = ref('')
async function fetchData() {
  const res = await api.getStockLogs({ page:1, pageSize:20, keyword:keyword.value, changeType:changeType.value })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(fetchData)
</script>