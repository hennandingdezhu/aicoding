<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>库存预警 <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <select v-model="warnType"><option value="">全部</option><option value="LOW">库存不足</option><option value="HIGH">库存过高</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>商品</th><th>仓库</th><th>当前库存</th><th>安全库存下限</th><th>上限</th><th>预警类型</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.productName }}</td>
            <td>{{ item.warehouseName }}</td>
            <td style="color:#ff4d4f;font-weight:bold">{{ item.quantity }}</td>
            <td>{{ item.minStock }}</td>
            <td>{{ item.maxStock }}</td>
            <td><span class="status-tag status-rejected">{{ item.warnType==='LOW'?'库存不足':'库存过高' }}</span></td>
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
const warnType = ref('')
async function fetchData() {
  const res = await api.getStockWarnings({ page:1, pageSize:20, warnType:warnType.value })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(fetchData)
</script>