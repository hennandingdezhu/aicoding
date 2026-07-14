<template>
  <div id="app-root">
    <nav class="top-nav">
      <router-link to="/">Dashboard</router-link>
      <router-link to="/records">Records</router-link>
      <router-link to="/settings">Settings</router-link>
    </nav>
    <router-view />
  </div>
</template>
<style scoped>
.top-nav { display: flex; gap: 24px; padding: 12px 24px; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.08); }
.top-nav a { padding: 6px 12px; border-radius: 4px; font-size: 14px; color: #555; }
.top-nav a:hover { color: #1890ff; background: #e6f7ff; }
.top-nav a.router-link-exact-active { color: #1890ff; font-weight: 600; }
</style>