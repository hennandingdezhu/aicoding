<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>{{ isEdit ? '查看盘点单' : '新增盘点单' }} <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="form-card">
      <div class="form-group"><label>仓库</label><select v-model="form.warehouseId"><option value="1">主仓库</option></select></div>
      <div class="form-group"><label>盘点人</label><input v-model="form.checkPerson" /></div>
      <div class="form-group"><label>备注</label><textarea v-model="form.remark"></textarea></div>
      <div class="form-group" v-if="isEdit"><label>盘点明细</label>
        <table><thead><tr><th>商品</th><th>账面数量</th><th>实盘数量</th><th>差异</th></tr></thead>
          <tbody><tr v-for="d in form.details" :key="d.productId">
            <td>{{ d.productName }}</td><td>{{ d.bookQty }}</td>
            <td><input v-model="d.actualQty" type="number" style="width:80px" /></td>
            <td>{{ d.actualQty - d.bookQty }}</td>
          </tr></tbody>
        </table>
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
const form = ref({ warehouseId:'', checkPerson:'', remark:'', details:[] })
onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const res = await api.getInventoryCheckById(route.params.id)
    form.value = { ...res.data, details: res.data.details || [] }
  }
})
async function handleSubmit() {
  if (isEdit.value) { await api.updateInventoryCheck(form.value) }
  else { await api.createInventoryCheck(form.value) }
  router.push('/inventory')
}
</script>