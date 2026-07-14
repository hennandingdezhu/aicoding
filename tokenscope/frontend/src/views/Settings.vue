<template>
  <div class="settings">
    <h2>Settings</h2>
    <div class="form-card">
      <div class="field">
        <label>Monthly Budget ()</label>
        <input v-model.number="budget.monthlyBudget" type="number" step="0.01" />
      </div>
      <div class="field">
        <label>Alert Threshold: {{ budget.alertThreshold }}%</label>
        <input v-model.number="budget.alertThreshold" type="range" min="0" max="100" />
      </div>
      <button @click="save">Save</button>
      <span v-if="saved" class="ok">Saved!</span>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getBudget, updateBudget } from '../api/index.js'
const budget = ref({ monthlyBudget:50.00, alertThreshold:80 })
const saved = ref(false)
onMounted(async () => {
  const r = await getBudget()
  budget.value = {
    monthlyBudget: r.data.monthlyBudget,
    alertThreshold: Number((r.data.alertThreshold * 100).toFixed(0))
  }
})
const save = async () => {
  await updateBudget({
    monthlyBudget: budget.value.monthlyBudget,
    alertThreshold: budget.value.alertThreshold / 100
  })
  saved.value = true; setTimeout(()=>saved.value=false,2000)
}
</script>
<style scoped>
.settings{padding:20px}
.form-card{background:#fff;padding:24px;border-radius:8px;box-shadow:0 1px 3px rgba(0,0,0,.1);max-width:480px}
.field{margin-bottom:16px}
.field label{display:block;font-size:14px;color:#555;margin-bottom:6px}
.field input[type="number"]{width:100%;padding:8px 12px;border:1px solid #d9d9d9;border-radius:4px;font-size:14px}
.field input[type="range"]{width:100%}
.form-card button{padding:8px 24px;background:#1890ff;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:14px}
.ok{color:#52c41a;margin-left:12px;font-size:14px}
</style>