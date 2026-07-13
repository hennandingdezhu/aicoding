<template>
  <div class="login-wrapper">
    <div class="mock-banner">⚠️ 模拟数据 - 仅供前端演示，不连接真实数据库</div>
    <div class="login-card">
      <h2>仓储管理系统 <span class="mock-badge">模拟</span></h2>
      <p class="login-hint">演示账号: admin / admin123</p>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="username" placeholder="请输入用户名" @keyup.enter="handleLogin" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
      </div>
      <div class="form-error" v-if="error">{{ error }}</div>
      <button class="btn btn-primary btn-block" @click="handleLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import * as api from '../api/mock/index.js'
const router = useRouter()
const username = ref('admin')
const password = ref('admin123')
const error = ref('')
const loading = ref(false)
async function handleLogin() {
  if (!username.value || !password.value) { error.value = '请输入用户名和密码'; return }
  loading.value = true; error.value = ''
  const res = await api.login(username.value, password.value)
  if (res.code === 200) {
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', res.data.user.realName)
    router.push('/')
  } else {
    error.value = res.message || '登录失败'
  }
  loading.value = false
}
</script>
<style scoped>
.login-wrapper{display:flex;flex-direction:column;align-items:center;justify-content:center;min-height:100vh;background:#f0f2f5}
.login-card{background:#fff;padding:40px;border-radius:8px;box-shadow:0 2px 12px rgba(0,0,0,.1);width:400px}
.login-card h2{text-align:center;margin-bottom:8px;color:#1890ff}
.login-hint{text-align:center;color:#999;font-size:13px;margin-bottom:20px}
.btn-block{width:100%;padding:10px;font-size:16px;margin-top:12px}
.form-error{color:#ff4d4f;font-size:13px;margin-bottom:8px}
</style>