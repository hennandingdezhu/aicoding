<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>操作日志 <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索操作人/描述..." />
        <select v-model="logType"><option value="">全部类型</option><option value="CREATE">创建</option><option value="UPDATE">更新</option><option value="DELETE">删除</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>操作人</th><th>操作类型</th><th>模块</th><th>描述</th><th>IP</th><th>时间</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.operator }}</td>
            <td><span :class="'status-tag status-' + (item.operationType==='DELETE'?'rejected':'approved')">{{ item.operationType }}</span></td>
            <td>{{ item.module }}</td>
            <td>{{ item.description }}</td>
            <td>{{ item.ipAddress }}</td>
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
const logType = ref('')
async function fetchData() {
  const res = await api.getOperationLogs({ page:1, pageSize:20, keyword:keyword.value, operationType:logType.value })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(fetchData)
</script>