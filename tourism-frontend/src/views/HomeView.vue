<template>
  <div class="home-page">
    <MainHeader />

    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-content">
        <h1>AI驱动的智能旅游推荐</h1>
        <p>基于人工智能和大数据，为您提供个性化的旅行规划、景点推荐和美食指南</p>
        <div class="hero-actions">
          <el-input
            v-model="searchKeyword"
            size="large"
            placeholder="搜索目的地、景点..."
            class="hero-search"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </div>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-number">{{ attractions.length }}+</span>
            <span class="stat-label">精品景点</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">50+</span>
            <span class="stat-label">热门城市</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">AI</span>
            <span class="stat-label">智能规划</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 热门景点 -->
    <section class="section">
      <div class="section-header">
        <h2>🔥 热门景点推荐</h2>
        <router-link to="/attractions" class="view-all">查看全部 <el-icon><ArrowRight /></el-icon></router-link>
      </div>
      <div class="attraction-grid">
        <el-card
          v-for="item in attractions"
          :key="item.id"
          :body-style="{ padding: '0' }"
          shadow="hover"
          class="attraction-card"
          @click="$router.push(`/attractions/${item.id}`)"
        >
          <div class="card-image">
            <img :src="item.imageUrl || defaultImg" :alt="item.name" @error="e => e.target.src = defaultImg" />
            <div class="card-tag">{{ item.city }}</div>
          </div>
          <div class="card-info">
            <h3>{{ item.name }}</h3>
            <p class="card-category">{{ item.category }}</p>
            <div class="card-bottom">
              <el-rate :model-value="Number(item.rating)" disabled text-color="#ff9900" size="small" />
              <span class="rating-text">{{ item.rating }}分</span>
            </div>
          </div>
        </el-card>
      </div>
    </section>

    <!-- 功能特色 -->
    <section class="section features">
      <h2>✨ 平台特色</h2>
      <div class="feature-grid">
        <div class="feature-card">
          <el-icon :size="40" color="#409eff"><MagicStick /></el-icon>
          <h3>AI行程规划</h3>
          <p>只需告诉AI您的偏好，即可生成专属旅行计划</p>
        </div>
        <div class="feature-card">
          <el-icon :size="40" color="#67c23a"><Location /></el-icon>
          <h3>海量景点</h3>
          <p>覆盖全国热门城市，精选优质景点信息</p>
        </div>
        <div class="feature-card">
          <el-icon :size="40" color="#e6a23c"><Star /></el-icon>
          <h3>真实评价</h3>
          <p>来自真实游客的评价和评分，助您做出最佳选择</p>
        </div>
        <div class="feature-card">
          <el-icon :size="40" color="#f56c6c"><Notebook /></el-icon>
          <h3>游记分享</h3>
          <p>记录旅行点滴，分享旅途中的美好瞬间</p>
        </div>
      </div>
    </section>

    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getHotAttractions } from '@/api/attraction'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()
const searchKeyword = ref('')
const attractions = ref([])
const defaultImg = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZTBlNmYwIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE4IiBmaWxsPSIjOTk5IiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBkeT0iLjNlbSI+5Zu+54mH5Yqg6L295aSx6LSlPC90ZXh0Pjwvc3ZnPg=='

onMounted(async () => {
  try {
    const res = await getHotAttractions()
    attractions.value = res.data || []
  } catch (e) {
    console.error('加载热门景点失败:', e)
  }
})

function handleSearch() {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/attractions', query: { keyword: searchKeyword.value.trim() } })
  }
}
</script>

<style scoped>
/* Hero */
.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 80px 20px;
  text-align: center;
}

.hero-content h1 {
  font-size: 42px;
  margin-bottom: 16px;
}

.hero-content > p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 40px;
}

.hero-search {
  max-width: 600px;
  margin: 0 auto;
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-top: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

/* Section通用 */
.section {
  max-width: 1400px;
  margin: 60px auto;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
}

.view-all {
  color: #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 景点网格 */
.attraction-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
}

@media (max-width: 1200px) {
  .attraction-grid { grid-template-columns: repeat(4, 1fr); }
}
@media (max-width: 900px) {
  .attraction-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 500px) {
  .attraction-grid { grid-template-columns: 1fr; }
}

.attraction-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.attraction-card:hover {
  transform: translateY(-4px);
}

.card-image {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(64, 158, 255, 0.85);
  color: #fff;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.card-info {
  padding: 16px;
}

.card-info h3 {
  font-size: 16px;
  margin-bottom: 4px;
}

.card-category {
  color: #999;
  font-size: 13px;
  margin-bottom: 10px;
}

.card-bottom {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rating-text {
  font-size: 13px;
  color: #ff9900;
}

/* 功能特色 */
.features h2 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 24px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

@media (max-width: 768px) {
  .feature-grid { grid-template-columns: repeat(2, 1fr); }
}

.feature-card {
  text-align: center;
  padding: 32px 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.feature-card h3 {
  margin: 16px 0 8px;
  font-size: 18px;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}
</style>
