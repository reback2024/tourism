<template>
  <div class="admin-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="220px">
        <div class="admin-logo">
          <h3>管理后台</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataAnalysis /></el-icon><span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/attractions">
            <el-icon><Location /></el-icon><span>景点管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/hotels">
            <el-icon><OfficeBuilding /></el-icon><span>酒店管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/restaurants">
            <el-icon><ForkSpoon /></el-icon><span>餐厅管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon><span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/reviews">
            <el-icon><ChatDotSquare /></el-icon><span>内容审核</span>
          </el-menu-item>
          <el-menu-item index="/admin/config">
            <el-icon><Setting /></el-icon><span>系统配置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="admin-header">
          <div class="admin-header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/admin' }">管理后台</el-breadcrumb-item>
              <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="admin-header-right">
            <el-button text @click="$router.push('/')">返回前台</el-button>
            <el-button text type="danger" @click="handleLogout">退出</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.admin-layout { min-height: 100vh; }
.admin-layout .el-container { min-height: 100vh; }
.el-aside { background: #304156; overflow: hidden; }
.admin-logo { padding: 20px; text-align: center; }
.admin-logo h3 { color: #fff; }
.admin-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  height: 56px;
}
.admin-header-left, .admin-header-right { display: flex; align-items: center; gap: 12px; }
</style>
