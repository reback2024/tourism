<template>
  <div class="detail-page">
    <MainHeader />

    <div class="page-content" v-if="attraction">
      <!-- 面包屑 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/attractions' }">景点</el-breadcrumb-item>
        <el-breadcrumb-item>{{ attraction.name }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 基本信息 -->
      <div class="detail-card">
        <div class="detail-main">
          <div class="detail-image">
            <img :src="attraction.imageUrl || defaultImg" :alt="attraction.name" @error="e => e.target.src = defaultImg" />
          </div>
          <div class="detail-info">
            <h1>{{ attraction.name }}</h1>
            <div class="info-tags">
              <el-tag type="primary">{{ attraction.city }}</el-tag>
              <el-tag>{{ attraction.category }}</el-tag>
            </div>
            <div class="info-rating">
              <el-rate :model-value="Number(attraction.rating)" disabled text-color="#ff9900" />
              <span class="rating-score">{{ attraction.rating }}分</span>
              <span class="rating-count">({{ attraction.reviewCount }}条评价)</span>
            </div>
            <div class="info-items">
              <div class="info-item" v-if="attraction.address">
                <el-icon><Location /></el-icon>
                <span>{{ attraction.address }}</span>
              </div>
              <div class="info-item" v-if="attraction.openingHours">
                <el-icon><Clock /></el-icon>
                <span>开放时间: {{ attraction.openingHours }}</span>
              </div>
              <div class="info-item">
                <el-icon><Money /></el-icon>
                <span v-if="attraction.ticketPrice > 0">门票: ¥{{ attraction.ticketPrice }}</span>
                <span v-else style="color: #67c23a">免费开放</span>
              </div>
            </div>
            <div class="info-actions">
              <el-button
                type="warning"
                :icon="attraction.isFavorited ? StarFilled : Star"
                @click="handleFavorite"
              >
                {{ attraction.isFavorited ? '已收藏' : '收藏' }}
              </el-button>
              <el-button
                v-if="userStore.isLoggedIn"
                type="success"
                :icon="MagicStick"
                @click="$router.push(`/itinerary/plan?destination=${attraction.city}`)"
              >
                AI规划行程
              </el-button>
            </div>
          </div>
        </div>

        <!-- 景点介绍 -->
        <div class="detail-section">
          <h3>景点介绍</h3>
          <p class="description">{{ attraction.description }}</p>
        </div>
      </div>

      <!-- 评价区域 -->
      <div class="review-section">
        <div class="review-header">
          <h3>游客评价</h3>
          <el-button v-if="userStore.isLoggedIn" type="primary" @click="showForm = true">写评价</el-button>
        </div>
        <div v-if="showForm" class="review-form">
          <el-rate v-model="form.rating" show-score />
          <el-input v-model="form.content" type="textarea" :rows="3" placeholder="分享你的游玩体验..." />
          <el-button type="primary" @click="submitReview" style="margin-top:10px">发表</el-button>
        </div>
        <div v-if="reviews.length === 0" style="text-align:center;color:#999;padding:40px">暂无评价，来第一个评价吧</div>
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="review-user"><span class="review-name">{{ r.username }}</span><el-rate :model-value="r.rating" disabled size="small" /></div>
          <p>{{ r.content }}</p>
          <span class="review-time">{{ r.createTime }}</span>
        </div>
      </div>
    </div>

    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getAttractionDetail } from '@/api/attraction'
import request from '@/api/request'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const attraction = ref(null)
const reviews = ref([])
const showForm = ref(false)
const form = reactive({ rating: 5, content: '' })
const defaultImg = 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600'

onMounted(async () => {
  try {
    const res = await getAttractionDetail(route.params.id)
    attraction.value = res.data
  } catch (e) { attraction.value = null }
  loadReviews()
})

async function loadReviews() {
  try {
    const res = await request({ url: '/reviews', params: { targetType: 'ATTRACTION', targetId: route.params.id } })
    reviews.value = res.data.records || []
  } catch {}
}

function handleFavorite() {
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录'); return }
  ElMessage.info('收藏功能即将上线')
}

async function submitReview() {
  try {
    await request({ url: '/reviews', method: 'post', data: { targetType: 'ATTRACTION', targetId: attraction.value.id, rating: form.rating, content: form.content } })
    ElMessage.success('评价成功')
    showForm.value = false
    form.content = ''
    loadReviews()
  } catch {}
}
</script>

<style scoped>
.detail-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.page-content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.detail-card {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
  margin-top: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.detail-main {
  display: flex;
  gap: 40px;
}

@media (max-width: 768px) {
  .detail-main { flex-direction: column; }
}

.detail-image {
  width: 500px;
  height: 360px;
  border-radius: 12px;
  overflow: hidden;
  flex-shrink: 0;
}

.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-info {
  flex: 1;
}

.detail-info h1 {
  font-size: 28px;
  margin-bottom: 12px;
}

.info-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.info-rating {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}

.rating-score { color: #ff9900; font-size: 18px; font-weight: bold; }
.rating-count { color: #999; font-size: 14px; }

.info-items { margin-bottom: 24px; }

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.info-actions {
  display: flex;
  gap: 12px;
}

.detail-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.detail-section h3 {
  font-size: 18px;
  margin-bottom: 16px;
}

.description {
  color: #555;
  line-height: 1.8;
  font-size: 15px;
}

.review-section {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
  margin-top: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.review-section h3 {
  font-size: 18px;
  margin-bottom: 16px;
}

.coming-soon {
  color: #999;
  text-align: center;
  padding: 40px 0;
}
</style>
