<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">📦 仓储管理系统</div>
      <nav>
        <router-link to="/">📊 首页</router-link>
        <div class="nav-group">
          <span class="nav-title">基础资料</span>
          <router-link to="/products">📦 商品管理</router-link>
          <router-link to="/categories">📁 商品分类</router-link>
          <router-link to="/suppliers">🏭 供应商管理</router-link>
          <router-link to="/customers">👥 客户管理</router-link>
        </div>
        <div class="nav-group">
          <span class="nav-title">仓库管理</span>
          <router-link to="/warehouses">🏗️ 仓库管理</router-link>
          <router-link to="/areas">📐 库区管理</router-link>
          <router-link to="/locations">📍 库位管理</router-link>
        </div>
        <div class="nav-group">
          <span class="nav-title">业务管理</span>
          <router-link to="/inbound">📥 入库管理</router-link>
          <router-link to="/outbound">📤 出库管理</router-link>
          <router-link to="/transfers">🔄 调拨管理</router-link>
          <router-link to="/inventory">📋 盘点管理</router-link>
        </div>
        <div class="nav-group">
          <span class="nav-title">库存管理</span>
          <router-link to="/stocks">📊 实时库存</router-link>
          <router-link to="/stock-logs">📝 库存流水</router-link>
          <router-link to="/stock-warnings">⚠️ 库存预警</router-link>
        </div>
        <div class="nav-group">
          <span class="nav-title">报表中心</span>
          <router-link to="/reports">📈 报表中心</router-link>
        </div>
        <div class="nav-group">
          <span class="nav-title">系统管理</span>
          <router-link to="/users">👤 用户管理</router-link>
          <router-link to="/roles">🔑 角色管理</router-link>
          <router-link to="/logs">📜 操作日志</router-link>
        </div>
      </nav>
      <div class="sidebar-footer">
        <span>{{ username }}</span>
        <button @click="logout" class="btn btn-default btn-sm">退出</button>
      </div>
    </aside>
    <main class="main">
      <slot />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref(localStorage.getItem('username') || 'admin')

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>

<style scoped>
.layout { display: flex; min-height: 100vh; }
.sidebar { width: 220px; background: #001529; color: #fff; display: flex; flex-direction: column; flex-shrink: 0; }
.logo { padding: 20px 16px; font-size: 16px; font-weight: bold; border-bottom: 1px solid #002140; }
nav { flex: 1; overflow-y: auto; padding: 8px 0; }
.nav-group { margin-bottom: 4px; }
.nav-title { display: block; padding: 8px 16px 4px; font-size: 11px; color: #8c8c8c; text-transform: uppercase; }
nav a { display: block; padding: 8px 24px; color: #a6a6a6; font-size: 14px; transition: all .2s; }
nav a:hover, nav a.router-link-active { color: #fff; background: #1890ff; }
.sidebar-footer { padding: 12px 16px; border-top: 1px solid #002140; display: flex; justify-content: space-between; align-items: center; font-size: 13px; }
.sidebar-footer .btn { background: transparent; color: #a6a6a6; border-color: #a6a6a6; }
.main { flex: 1; padding: 0; overflow-y: auto; }
</style>