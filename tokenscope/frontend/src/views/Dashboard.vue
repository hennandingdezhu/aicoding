<template>
  <div class="dashboard">
    <h1>TokenScope <span style="font-size:14px;color:#999">deepseek-v4-pro</span></h1>
    <div class="stat-cards">
      <div class="card"><div class="num">{stats.currentCost||'0'}</div><div class="label">Cost This Month</div></div>
      <div class="card"><div class="num">{stats.monthlyBudget||'50.00'}</div><div class="label">Monthly Budget</div></div>
      <div class="card" :class="{over: stats.isOverBudget}"><div class="num">{{ stats.percentage||0 }}%</div><div class="label">Budget Used</div></div>
    </div>
    <div class="progress-bar"><div class="fill" :style="{width: (stats.percentage||0)+'%'}"></div></div>
    <div ref="chart" style="width:100%;height:300px;margin-top:20px"></div>
  </div>
</template>
<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getStats, getDailyTrend } from '../api/index.js'
const stats = ref({currentCost:'0', monthlyBudget:'50.00', percentage:0, isOverBudget:false})
const chart = ref(null)
onMounted(async () => {
  const s = await getStats(); stats.value = s.data
  const t = await getDailyTrend()
  await nextTick()
  const c = echarts.init(chart.value)
  c.setOption({ xAxis:{type:'category',data:t.data.map(d=>d.date)}, yAxis:{type:'value'}, series:[{data:t.data.map(d=>d.cost),type:'line',smooth:true}] })
})
</script>
<style scoped>
.dashboard{padding:20px}
.stat-cards{display:flex;gap:16px;margin:16px 0}
.card{flex:1;background:#fff;padding:20px;border-radius:8px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.1)}
.card .num{font-size:28px;font-weight:bold;color:#1890ff}
.card .label{font-size:13px;color:#999;margin-top:4px}
.card.over .num{color:#ff4d4f}
.progress-bar{height:12px;background:#f0f0f0;border-radius:6px;overflow:hidden}
.progress-bar .fill{height:100%;background:#52c41a;transition:width .5s}
</style>