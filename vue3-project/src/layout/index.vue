<template>
  <el-container class="layout-container">
    <!-- 侧边栏 (仅登录后显示) -->
    <el-aside v-if="user" :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo-box">
        <el-icon :size="24" color="#409EFF"><ShoppingBag /></el-icon>
        <span v-show="!isCollapse">{{ platformTitle }}</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        :collapse="isCollapse"
        router
        background-color="#001529"
        text-color="#ffffffb3"
        active-text-color="#fff"
      >
        <el-menu-item v-if="role !== 'USER'" index="/home">
          <el-icon><DataBoard /></el-icon>
          <template #title>统计看板</template>
        </el-menu-item>

        <el-menu-item v-if="role === 'USER'" index="/product/list">
          <el-icon><ShoppingBag /></el-icon>
          <template #title>商品商城</template>
        </el-menu-item>

        <!-- 管理员/超管 可见 -->
        <el-sub-menu v-if="role !== 'USER'" index="1">
          <template #title>
            <el-icon><Box /></el-icon>
            <span>商品库存</span>
          </template>
          <el-menu-item index="/product/manage">
            <el-icon><Goods /></el-icon>商品管理
          </el-menu-item>
          <el-menu-item index="/product/stock">
            <el-icon><List /></el-icon>库存流水
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item v-if="role === 'SUPER_ADMIN'" index="/order/manage">
          <el-icon><Document /></el-icon>
          <template #title>订单审核</template>
        </el-menu-item>

        <!-- 只有超管可见 -->
        <el-sub-menu v-if="role === 'SUPER_ADMIN'" index="2">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/admin-manage">
            <el-icon><UserFilled /></el-icon>管理员管理
          </el-menu-item>
          <el-menu-item index="/finance/report">
            <el-icon><TrendCharts /></el-icon>财务报表
          </el-menu-item>
          <el-menu-item index="/product/category">
            <el-icon><Collection /></el-icon>分类管理
          </el-menu-item>
        </el-sub-menu>

        <!-- 只有普通客户可见 -->
        <el-menu-item v-if="role === 'USER'" index="/order/my">
          <el-icon><Tickets /></el-icon>
          <template #title>我的订单</template>
        </el-menu-item>

        <el-menu-item index="/profile">
          <el-icon><User /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon @click="isCollapse = !isCollapse" class="fold-btn">
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <template v-if="user">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
                <span class="username">{{ user.realName || user.username }}</span>
                <el-tag size="small" :type="roleType">{{ roleName }}</el-tag>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <div class="user-info guest-mode" @click="router.push('/login')">
              <el-avatar :size="32" icon="User" />
              <span class="username">请登录</span>
              <el-tag size="small" type="info">游客模式</el-tag>
            </div>
          </template>
        </div>
      </el-header>

      <!-- 主体内容 -->
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const userJson = localStorage.getItem('erp-user')
const user = userJson ? JSON.parse(userJson) : null

const role = computed(() => user ? user.role : null)
const activeMenu = computed(() => route.path)

const roleName = computed(() => {
  if (!user) return '游客'
  if (role.value === 'SUPER_ADMIN') return '超级管理员'
  if (role.value === 'ADMIN') return '管理员'
  return '普通用户'
})

const roleType = computed(() => {
  if (role.value === 'SUPER_ADMIN') return 'danger'
  if (role.value === 'ADMIN') return 'warning'
  return 'info'
})

const platformTitle = computed(() => {
  return role.value === 'USER' ? 'X-ERP 购物平台' : 'X-ERP 管理平台'
})

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出系统吗？', '提示', {
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('erp-user')
      router.push('/login')
    })
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

// 如果是普通用户，且当前在首页统计看板，自动跳转到商城
if (role.value === 'USER' && route.path === '/home') {
  router.push('/product/list')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #001529;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo-box {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  gap: 10px;
  background: #002140;
}

.el-menu-vertical {
  border-right: none;
}

.header {
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.fold-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f5f7f9;
}

.guest-mode {
  color: #409EFF;
}

.main {
  background: #f5f7f9;
  padding: 20px;
}

/* 页面切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
