<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>{{ isEdit ? '编辑商品' : '新增商品' }} <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="form-card">
      <div class="form-group"><label>商品编码</label><input v-model="form.productCode" placeholder="请输入商品编码" /></div>
      <div class="form-group"><label>商品名称</label><input v-model="form.productName" placeholder="请输入商品名称" /></div>
      <div class="form-group"><label>分类</label><input v-model="form.categoryName" placeholder="请输入分类" /></div>
      <div class="form-group"><label>规格</label><input v-model="form.spec" placeholder="请输入规格" /></div>
      <div class="form-group"><label>单位</label><input v-model="form.unit" placeholder="请输入单位" /></div>
      <div class="form-group"><label>状态</label>
        <select v-model="form.status"><option :value="1">启用</option><option :value="0">禁用</option></select>
      </div>
      <div style="display:flex;gap:12px">
        <button class="btn btn-primary" @click="handleSubmit">提交</button>
        <button class="btn btn-default" @click="$router.back()">取消</button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as api from '../../api/index.js'
const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const form = ref({ productCode:'', productName:'', categoryName:'', spec:'', unit:'', status:1 })
onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const res = await api.getProductById(route.params.id)
    Object.assign(form.value, res.data)
  }
})
async function handleSubmit() {
  if (isEdit.value) { await api.updateProduct(form.value) }
  else { await api.createProduct(form.value) }
  router.push('/products')
}
</script>