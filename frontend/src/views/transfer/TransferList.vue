<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>调拨管理 <span class="mock-badge">模拟</span></h2>
      <button class="btn btn-primary" @click="$router.push('/transfers/create')">新增调拨单</button>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索单号..." />
        <select v-model="filterStatus"><option value="">全部状态</option><option value="DRAFT">草稿</option><option value="PENDING">待审核</option><option value="APPROVED">已审核</option><option value="COMPLETED">已完成</option></select>
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>单号</th><th>源仓库</th><th>目标仓库</th><th>商品</th><th>数量</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.orderNo }}</td>
            <td>{{ item.sourceWarehouseName }}</td>
            <td>{{ item.targetWarehouseName }}</td>
            <td>{{ item.productName }}</td>
            <td>{{ item.quantity }}</td>
            <td><span :class="'status-tag status-' + item.status.toLowerCase()">{{ statusMap[item.status] || item.status }}</span></td>
            <td>
              <template v-if="item.status==='DRAFT'">
                <button class="btn btn-primary btn-sm" @click="handleSubmit(item.id)">提交审核</button>
                <button class="btn btn-danger btn-sm" @click="handleDelete(item.id)">删除</button>
              </template>
              <template v-else-if="item.status==='PENDING'">
                <button class="btn btn-primary btn-sm" @click="handleAudit(item.id,'APPROVED')">审核通过</button>
                <button class="btn btn-danger btn-sm" @click="handleAudit(item.id,'REJECTED')">驳回</button>
              </template>
              <template v-else-if="item.status==='APPROVED'">
                <button class="btn btn-primary btn-sm" @click="handleConfirm(item.id)">确认调拨</button>
              </template>
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
const statusMap = { DRAFT:'草稿', PENDING:'待审核', APPROVED:'已审核', REJECTED:'已驳回', COMPLETED:'已完成' }
async function fetchData() {
  const res = await api.getTransferOrders({ page:1, pageSize:20, keyword:keyword.value, status:filterStatus.value })
  list.value = res.data.records; total.value = res.data.total
}
async function handleSubmit(id) { await api.submitTransferOrder(id); fetchData() }
async function handleAudit(id, status) { await api.auditTransferOrder(id, status); fetchData() }
async function handleConfirm(id) { await api.confirmTransferOrder(id); fetchData() }
async function handleDelete(id) { if (confirm('确定删除？')) { await api.deleteTransferOrder(id); fetchData() } }
onMounted(fetchData)
</script>