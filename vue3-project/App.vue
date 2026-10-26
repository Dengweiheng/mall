<template>
  <router-view />
</template>

<script setup>
</script>

<style>
/* 全局样式覆盖 */
body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  overflow: hidden;
}

#app {
  height: 100vh;
}

/* 统一的一些工具类 */
.price {
  color: #f56c6c;
  font-weight: 500;
}
</style>
