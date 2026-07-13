<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>用户管理 <span class="mock-badge">模拟</span></h2>
      <button class="btn btn-primary" @click="$router.push('/users/create')">新增用户</button>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索用户名..." />
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>用户名</th><th>昵称</th><th>角色</th><th>状态</th><th>创建时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.username }}</td>
            <td>{{ item.nickname }}</td>
            <td>{{ item.roleName }}</td>
            <td><span :class="'status-tag status-' + (item.status===1?'confirmed':'cancelled')">{{ item.status===1?'启用':'禁用' }}</span></td>
            <td>{{ item.createTime }}</td>
            <td>
              <button class="btn btn-default btn-sm" @click="$router.push('/users/'+item.id+'/edit')">编辑</button>
              <button class="btn btn-danger btn-sm" @click="handleDelete(item.id)">删除</button>
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
async function fetchData() {
  const res = await api.getUsers({ page:1, pageSize:20, keyword:keyword.value })
  list.value = res.data.records; total.value = res.data.total
}
async function handleDelete(id) { if (confirm('确定删除？')) { await api.deleteUser(id); fetchData() } }
onMounted(fetchData)
</script>