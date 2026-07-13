<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>盘点管理 <span class="mock-badge">模拟</span></h2>
      <button class="btn btn-primary" @click="$router.push('/inventory/create')">新增盘点单</button>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索单号..." />
        <select v-model="filterStatus"><option value="">全部状态</option><option value="DRAFT">草稿</option><option value="CHECKING">盘点中</option><option value="COMPLETED">已完成</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>单号</th><th>仓库</th><th>盘点人</th><th>盘点数量</th><th>差异数量</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.checkNo }}</td>
            <td>{{ item.warehouseName }}</td>
            <td>{{ item.checkPerson }}</td>
            <td>{{ item.checkQty }}</td>
            <td>{{ item.diffQty }}</td>
            <td><span :class="'status-tag status-' + item.status.toLowerCase()">{{ statusMap[item.status] || item.status }}</span></td>
            <td>
              <template v-if="item.status==='DRAFT'">
                <button class="btn btn-primary btn-sm" @click="handleStart(item.id)">开始盘点</button>
              </template>
              <template v-else-if="item.status==='CHECKING'">
                <button class="btn btn-primary btn-sm" @click="handleComplete(item.id)">完成盘点</button>
              </template>
              <button class="btn btn-default btn-sm" @click="$router.push('/inventory/'+item.id)">详情</button>
            </td>
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
const filterStatus = ref('')
const statusMap = { DRAFT:'草稿', CHECKING:'盘点中', COMPLETED:'已完成' }
async function fetchData() {
  const res = await api.getInventoryChecks({ page:1, pageSize:20, keyword:keyword.value, status:filterStatus.value })
  list.value = res.data.records; total.value = res.data.total
}
async function handleStart(id) { await api.startInventoryCheck(id); fetchData() }
async function handleComplete(id) { await api.completeInventoryCheck(id); fetchData() }
onMounted(fetchData)
</script>