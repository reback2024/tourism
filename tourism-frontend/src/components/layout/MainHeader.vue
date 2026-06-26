<template>
  <header class="main-header">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <div class="logo-icon">🏔</div>
        <span class="logo-text">智游</span>
      </router-link>

      <nav class="nav-links">
        <router-link to="/attractions" class="nav-item">🏛 景点</router-link>
        <router-link to="/hotels" class="nav-item">🏨 酒店</router-link>
        <router-link to="/restaurants" class="nav-item">🍜 美食</router-link>
        <router-link to="/travel-notes" class="nav-item">📝 游记</router-link>
        <router-link to="/itinerary/plan" class="nav-item ai-link">✨ AI规划</router-link>
      </nav>

      <div class="header-right">
        <div class="search-box" :class="{ focused: searchFocused }">
          <el-icon><Search /></el-icon>
          <input v-model="keyword" placeholder="搜索目的地..." @focus="searchFocused=true" @blur="searchFocused=false" @keyup.enter="doSearch" />
        </div>

        <template v-if="userStore.isLoggedIn">
          <el-dropdown trigger="click">
            <div class="user-chip">
              <el-avatar :size="34" :src="userStore.user?.avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="user-name">{{ userStore.nickname }}</span>
              <el-icon class="chevron"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/user/profile')">👤 个人中心</el-dropdown-item>
                <el-dropdown-item @click="$router.push('/user/favorites')">⭐ 我的收藏</el-dropdown-item>
                <el-dropdown-item @click="$router.push('/itinerary/list')">📋 我的行程</el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" @click="$router.push('/admin')" divided>⚙️ 管理后台</el-dropdown-item>
                <el-dropdown-item divided @click="doLogout">🚪 退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <div class="auth-btns">
            <el-button round @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" round @click="$router.push('/register')">注册</el-button>
          </div>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter(); const userStore = useUserStore()
const keyword = ref(''); const searchFocused = ref(false)

function doSearch() {
  if (keyword.value.trim()) router.push({ path: '/attractions', query: { keyword: keyword.value.trim() } })
}
function doLogout() { userStore.logout(); ElMessage.success('已退出'); router.push('/') }
</script>

<style scoped>
.main-header {
  position: sticky; top: 0; z-index: 1000;
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(0,0,0,0.06);
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}
.header-inner {
  max-width: 1400px; margin: 0 auto;
  display: flex; align-items: center; height: 64px; padding: 0 24px; gap: 24px;
}
.logo { display: flex; align-items: center; gap: 8px; flex-shrink: 0; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 22px; font-weight: 800; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.nav-links { display: flex; gap: 4px; }
.nav-item {
  padding: 8px 16px; border-radius: 10px; font-size: 14px; color: #555;
  transition: all 0.2s; font-weight: 500;
}
.nav-item:hover, .nav-item.router-link-active { background: #f0f0ff; color: #667eea; }
.ai-link { background: linear-gradient(135deg, #fef3e2, #fff0f0); color: #e6a23c !important; }
.ai-link:hover { background: linear-gradient(135deg, #fde6b8, #ffe0e0) !important; }
.header-right { display: flex; align-items: center; gap: 16px; margin-left: auto; }
.search-box {
  display: flex; align-items: center; gap: 8px; padding: 8px 16px;
  background: #f5f6f8; border-radius: 24px; border: 2px solid transparent;
  transition: all 0.3s; width: 220px;
}
.search-box.focused { background: #fff; border-color: #667eea; width: 280px; box-shadow: 0 0 0 4px rgba(102,126,234,0.1); }
.search-box input { border: none; outline: none; background: transparent; font-size: 14px; width: 100%; color: #333; }
.search-box .el-icon { color: #999; font-size: 18px; }
.user-chip { display: flex; align-items: center; gap: 8px; cursor: pointer; padding: 4px 12px 4px 4px; border-radius: 20px; background: #f5f6f8; transition: all 0.2s; }
.user-chip:hover { background: #eef0ff; }
.user-name { font-size: 14px; font-weight: 500; }
.chevron { font-size: 12px; color: #999; }
.auth-btns { display: flex; gap: 8px; }
@media (max-width: 1000px) { .nav-links { display: none; } }
@media (max-width: 600px) { .search-box { display: none; } }
</style>
