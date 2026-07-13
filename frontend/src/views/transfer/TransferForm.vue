<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>{{ isEdit ? '查看调拨单' : '新增调拨单' }} <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="form-card">
      <div class="form-group"><label>源仓库</label><select v-model="form.sourceWarehouseId"><option value="1">主仓库</option></select></div>
      <div class="form-group"><label>目标仓库</label><select v-model="form.targetWarehouseId"><option value="2">分仓</option></select></div>
      <div class="form-group"><label>商品</label><select v-model="form.productId"><option value="1">PRODUCT-001 商品A</option><option value="2">PRODUCT-002 商品B</option></select></div>
      <div class="form-group"><label>数量</label><input v-model="form.quantity" type="number" /></div>
      <div class="form-group"><label>备注</label><textarea v-model="form.remark"></textarea></div>
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
const form = ref({ sourceWarehouseId:'', targetWarehouseId:'', productId:'', quantity:0, remark:'' })
onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const res = await api.getTransferOrderById(route.params.id)
    Object.assign(form.value, res.data)
  }
})
async function handleSubmit() {
  await api.createTransferOrder(form.value)
  router.push('/transfers')
}
</script>