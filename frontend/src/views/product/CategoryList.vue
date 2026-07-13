<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>分类管理 <span class="mock-badge">模拟</span></h2>
      <button class="btn btn-primary" @click="showForm=true;resetForm()">新增分类</button>
    </div>
    <div class="table-card">
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索分类名称..." />
        <button class="btn btn-primary" @click="fetchData">搜索</button>
      </div>
      <table>
        <thead><tr><th>编码</th><th>名称</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.categoryCode }}</td>
            <td>{{ item.categoryName }}</td>
            <td>
              <button class="btn btn-default btn-sm" @click="editItem(item)">编辑</button>
              <button class="btn btn-danger btn-sm" @click="handleDelete(item.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination"><span>共 {{ total }} 条</span></div>
    </div>
    <div v-if="showForm" class="form-card" style="margin-top:16px">
      <h3 style="margin-bottom:12px">{{ editId?'编辑分类':'新增分类' }}</h3>
      <div class="form-group"><label>分类编码</label><input v-model="form.categoryCode" /></div>
      <div class="form-group"><label>分类名称</label><input v-model="form.categoryName" /></div>
      <div style="display:flex;gap:12px">
        <button class="btn btn-primary" @click="handleSubmit">提交</button>
        <button class="btn btn-default" @click="showForm=false">取消</button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import * as api from '../../api/index.js'
const list = ref([])
const total = ref(0)
const keyword = ref('')
const showForm = ref(false)
const editId = ref(null)
const form = ref({ categoryCode:'', categoryName:'' })
function resetForm() { editId.value=null; form.value={ categoryCode:'', categoryName:'' } }
function editItem(item) { editId.value=item.id; form.value={...item}; showForm.value=true }
async function fetchData() {
  const res = await api.getCategories({ page:1, pageSize:20, keyword:keyword.value })
  list.value = res.data.records; total.value = res.data.total
}
async function handleSubmit() {
  if (editId.value) { await api.updateCategory(editId.value, form.value) }
  else { await api.createCategory(form.value) }
  showForm.value=false; fetchData()
}
async function handleDelete(id) { if (confirm('确定删除？')) { await api.deleteCategory(id); fetchData() } }
onMounted(fetchData)
</script>