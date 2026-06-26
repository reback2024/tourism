<template>
  <el-header class="main-header">
    <div class="header-container">
      <!-- Logo -->
      <router-link to="/" class="logo">
        <el-icon :size="28"><Sunny /></el-icon>
        <span class="logo-text">智能旅游</span>
      </router-link>

      <!-- 导航菜单 -->
      <el-menu
        mode="horizontal"
        :default-active="activeIndex"
        :ellipsis="false"
        class="nav-menu"
        router
      >
        <el-menu-item index="/">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/attractions">
          <el-icon><Location /></el-icon>
          <span>景点</span>
        </el-menu-item>
        <el-menu-item index="/hotels">
          <el-icon><OfficeBuilding /></el-icon>
          <span>酒店</span>
        </el-menu-item>
        <el-menu-item index="/restaurants">
          <el-icon><ForkSpoon /></el-icon>
          <span>美食</span>
        </el-menu-item>
        <el-menu-item index="/travel-notes">
          <el-icon><Notebook /></el-icon>
          <span>游记</span>
        </el-menu-item>
      </el-menu>

      <!-- 右侧操作区 -->
      <div class="header-right">
        <!-- 搜索框 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索景点、城市..."
          :prefix-icon="Search"
          class="search-input"
          @keyup.enter="handleSearch"
        />

        <!-- 用户菜单 -->
        <template v-if="userStore.isLoggedIn">
          <router-link to="/itinerary/plan" class="ai-btn">
            <el-button type="warning" :icon="MagicStick" round size="small">
              AI规划
            </el-button>
          </router-link>

          <el-dropdown trigger="click">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="userStore.user?.avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="nickname">{{ userStore.nickname }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/user/profile')">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item @click="$router.push('/user/favorites')">
                  <el-icon><Star /></el-icon>我的收藏
                </el-dropdown-item>
                <el-dropdown-item @click="$router.push('/itinerary/list')">
                  <el-icon><List /></el-icon>我的行程
                </el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" @click="$router.push('/admin')" divided>
                  <el-icon><Setting /></el-icon>管理后台
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>

        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')

const activeIndex = computed(() => {
  const path = route.path
  if (path.startsWith('/attractions')) return '/attractions'
  if (path.startsWith('/hotels')) return '/hotels'
  if (path.startsWith('/restaurants')) return '/restaurants'
  if (path.startsWith('/travel-notes')) return '/travel-notes'
  return '/'
})

function handleSearch() {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/attractions', query: { keyword: searchKeyword.value.trim() } })
  }
}

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.main-header {
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 0;
  height: 64px;
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409eff;
  font-size: 20px;
  font-weight: bold;
  margin-right: 30px;
  white-space: nowrap;
}

.nav-menu {
  flex: 1;
  border-bottom: none !important;
}

.nav-menu .el-menu-item {
  border-bottom: 2px solid transparent;
}

.nav-menu .el-menu-item.is-active {
  border-bottom-color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input {
  width: 220px;
}

.ai-btn {
  text-decoration: none;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-dropdown:hover {
  background: #f5f7fa;
}

.nickname {
  font-size: 14px;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
