<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>库存管理 <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索商品名称..." />
        <select v-model="warehouseId"><option value="">全部仓库</option><option value="1">主仓库</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>商品编码</th><th>商品名称</th><th>仓库</th><th>库位</th><th>库存数量</th><th>可用数量</th><th>锁定数量</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.productCode }}</td>
            <td>{{ item.productName }}</td>
            <td>{{ item.warehouseName }}</td>
            <td>{{ item.locationCode }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.availableQty }}</td>
            <td>{{ item.lockedQty }}</td>
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
const warehouseId = ref('')
async function fetchData() {
  const res = await api.getStocks({ page:1, pageSize:20, keyword:keyword.value, warehouseId:warehouseId.value })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(fetchData)
</script>