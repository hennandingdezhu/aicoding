<template>
  <Login v-if="$route.path === '/login'" />
  <Layout v-else>
    <router-view />
  </Layout>
</template>

<script setup>
import Login from './views/Login.vue'
import Layout from './components/Layout.vue'
</script>