<template>
  <div class="record-list">
    <h2>Token Records</h2>
    <div class="form-card">
      <h3>New Record</h3>
      <div class="form-row">
        <select v-model="form.modelName">
          <option value="">Select Model</option>
          <option value="deepseek-v4-pro">deepseek-v4-pro</option>
          <option value="gpt-4">gpt-4</option>
          <option value="gpt-3.5-turbo">gpt-3.5-turbo</option>
        </select>
        <input v-model.number="form.promptTokens" type="number" placeholder="Prompt Tokens" />
        <input v-model.number="form.completionTokens" type="number" placeholder="Completion Tokens" />
        <input v-model="form.description" placeholder="Description" />
        <button @click="submitRecord">Add</button>
      </div>
    </div>
    <table>
      <thead>
        <tr>
          <th>Model</th>
          <th>Prompt Tokens</th>
          <th>Completion Tokens</th>
          <th>Total</th>
          <th>Cost</th>
          <th>Description</th>
          <th>Date</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in records" :key="r.id">
          <td>{{ r.modelName }}</td>
          <td>{{ r.promptTokens }}</td>
          <td>{{ r.completionTokens }}</td>
          <td>{{ r.totalTokens || (r.promptTokens + r.completionTokens) }}</td>
          <td>{r.cost}</td>
          <td>{{ r.description }}</td>
          <td>{{ r.createdAt }}</td>
          <td><button class="btn-del" @click="delRecord(r.id)">Delete</button></td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button :disabled="page<=1" @click="page--; fetch()">Prev</button>
      <span>Page {{ page }}</span>
      <button @click="page++; fetch()">Next</button>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getRecords, createRecord, deleteRecord } from '../api/index.js'
const records = ref([])
const page = ref(1)
const form = ref({ modelName:'', promptTokens:0, completionTokens:0, description:'' })
const fetch = async () => { const r = await getRecords(page.value); records.value = r.data.content }
const submitRecord = async () => { await createRecord(form.value); form.value = { modelName:'', promptTokens:0, completionTokens:0, description:'' }; fetch() }
const delRecord = async (id) => { await deleteRecord(id); fetch() }
onMounted(fetch)
</script>
<style scoped>
.record-list{padding:20px}
.form-card{background:#fff;padding:16px;border-radius:8px;margin:16px 0;box-shadow:0 1px 3px rgba(0,0,0,.1)}
.form-row{display:flex;gap:8px;flex-wrap:wrap}
.form-row select,.form-row input{padding:6px 10px;border:1px solid #d9d9d9;border-radius:4px;font-size:14px}
.form-row button{padding:6px 16px;background:#1890ff;color:#fff;border:none;border-radius:4px;cursor:pointer}
table{width:100%;background:#fff;border-radius:8px;box-shadow:0 1px 3px rgba(0,0,0,.1);border-collapse:collapse}
th,td{padding:10px 12px;text-align:left;border-bottom:1px solid #f0f0f0;font-size:13px}
th{background:#fafafa;font-weight:600}
.btn-del{padding:4px 10px;background:#ff4d4f;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:12px}
.pagination{display:flex;gap:12px;align-items:center;margin-top:16px}
.pagination button{padding:6px 14px;border:1px solid #d9d9d9;background:#fff;border-radius:4px;cursor:pointer}
.pagination button:disabled{opacity:.5;cursor:not-allowed}
</style>