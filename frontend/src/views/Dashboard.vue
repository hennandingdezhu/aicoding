<template>
  <div>
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="page-header">
      <h2>仪表盘 <span class="mock-badge">模拟</span></h2>
    </div>
    <div class="stat-grid">
      <div class="stat-card"><div class="num">2</div><div class="label">商品总数</div></div>
      <div class="stat-card"><div class="num">130</div><div class="label">库存总量</div></div>
      <div class="stat-card"><div class="num">150</div><div class="label">本月入库</div></div>
      <div class="stat-card"><div class="num">30</div><div class="label">本月出库</div></div>
    </div>
    <div class="table-card">
      <h3 style="margin-bottom:12px">最近操作日志</h3>
      <table>
        <thead><tr><th>操作人</th><th>操作类型</th><th>描述</th><th>时间</th></tr></thead>
        <tbody>
          <tr><td>admin</td><td>入库</td><td>商品入库: PRODUCT-001 x50</td><td>2026-07-13 10:30</td></tr>
          <tr><td>admin</td><td>出库</td><td>商品出库: PRODUCT-002 x10</td><td>2026-07-13 09:15</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script setup>
</script>