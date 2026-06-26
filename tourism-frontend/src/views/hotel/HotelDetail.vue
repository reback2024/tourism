<template>
  <div class="detail-page">
    <MainHeader />
    <div class="page-content" v-if="hotel">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/hotels' }">酒店</el-breadcrumb-item>
        <el-breadcrumb-item>{{ hotel.name }}</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="detail-card">
        <div class="detail-main">
          <div class="detail-image"><img :src="hotel.imageUrl || defaultImg" :alt="hotel.name" @error="e => e.target.src = defaultImg" /></div>
          <div class="detail-info">
            <h1>{{ hotel.name }}</h1>
            <div class="info-tags">
              <el-tag type="warning">{{ '⭐'.repeat(hotel.starLevel) }} {{ hotel.starLevel }}星级</el-tag>
              <el-tag type="primary">{{ hotel.city }}</el-tag>
            </div>
            <div class="info-rating">
              <span class="rating-score">{{ hotel.rating }}分</span>
              <span class="rating-count">({{ hotel.reviewCount }}条评价)</span>
            </div>
            <div class="info-items">
              <div class="info-item" v-if="hotel.address">📍 {{ hotel.address }}</div>
              <div class="info-item" v-if="hotel.phone">📞 {{ hotel.phone }}</div>
              <div class="info-item">💰 {{ hotel.priceRange || `¥${hotel.priceMin}-¥${hotel.priceMax}` }}</div>
            </div>
            <div class="info-actions">
              <el-button :type="isFav ? 'warning' : 'default'" @click="toggleFav">{{ isFav ? '已收藏' : '收藏' }}</el-button>
            </div>
          </div>
        </div>
        <div class="detail-section">
          <h3>酒店介绍</h3>
          <p class="description">{{ hotel.description }}</p>
        </div>
      </div>
      <!-- 评价区域 -->
      <div class="review-section">
        <div class="review-header">
          <h3>住客评价</h3>
          <el-button v-if="userStore.isLoggedIn" type="primary" @click="showReviewForm = true">写评价</el-button>
        </div>
        <div v-if="showReviewForm" class="review-form">
          <el-rate v-model="reviewForm.rating" show-score />
          <el-input v-model="reviewForm.content" type="textarea" :rows="3" placeholder="分享你的入住体验..." />
          <el-button type="primary" @click="submitReview" style="margin-top:10px">发表</el-button>
        </div>
        <div v-if="reviews.length === 0" style="text-align:center;padding:40px 0;color:#999">暂无评价</div>
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="review-user">
            <span class="review-name">{{ r.username }}</span>
            <el-rate :model-value="r.rating" disabled size="small" />
          </div>
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
import { getHotelDetail } from '@/api/hotel'
import request from '@/api/request'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const route = useRoute(); const userStore = useUserStore()
const hotel = ref(null); const reviews = ref([]); const isFav = ref(false); const showReviewForm = ref(false)
const defaultImg = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZTBlNmYwIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE4IiBmaWxsPSIjOTk5IiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBkeT0iLjNlbSI+5YWo5bqU5Zu+54mHPC90ZXh0Pjwvc3ZnPg=='
const reviewForm = reactive({ rating: 5, content: '' })

onMounted(async () => {
  try {
    const res = await getHotelDetail(route.params.id)
    hotel.value = res.data
  } catch {}
  loadReviews()
})

async function loadReviews() {
  try {
    const res = await request({ url: '/reviews', params: { targetType: 'HOTEL', targetId: route.params.id } })
    reviews.value = res.data.records || []
  } catch {}
}

async function toggleFav() {
  if (!userStore.isLoggedIn) return ElMessage.warning('请先登录')
  try {
    if (isFav.value) {
      await request({ url: '/favorites', method: 'delete', data: { targetType: 'HOTEL', targetId: hotel.value.id } })
      isFav.value = false; ElMessage.success('已取消')
    } else {
      await request({ url: '/favorites', method: 'post', data: { targetType: 'HOTEL', targetId: hotel.value.id } })
      isFav.value = true; ElMessage.success('已收藏')
    }
  } catch {}
}

async function submitReview() {
  try {
    await request({ url: '/reviews', method: 'post', data: { targetType: 'HOTEL', targetId: hotel.value.id, rating: reviewForm.rating, content: reviewForm.content } })
    ElMessage.success('评价成功')
    showReviewForm.value = false
    reviewForm.content = ''
    loadReviews()
  } catch {}
}
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1200px; margin: 20px auto; padding: 0 20px; }
.detail-card { background: #fff; border-radius: 12px; padding: 32px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,.06); }
.detail-main { display: flex; gap: 40px; }
@media (max-width: 768px) { .detail-main { flex-direction: column; } }
.detail-image { width: 500px; height: 360px; border-radius: 12px; overflow: hidden; flex-shrink: 0; }
.detail-image img { width: 100%; height: 100%; object-fit: cover; }
.detail-info { flex: 1; }
.detail-info h1 { font-size: 28px; margin-bottom: 12px; }
.info-tags { display: flex; gap: 8px; margin-bottom: 16px; }
.rating-score { color: #ff9900; font-size: 18px; font-weight: bold; }
.rating-count { color: #999; font-size: 14px; margin-left: 8px; }
.info-items { margin-bottom: 24px; }
.info-item { margin-bottom: 8px; color: #666; font-size: 14px; }
.info-actions { display: flex; gap: 12px; }
.detail-section { margin-top: 32px; padding-top: 24px; border-top: 1px solid #eee; }
.detail-section h3 { font-size: 18px; margin-bottom: 16px; }
.description { color: #555; line-height: 1.8; font-size: 15px; }
.review-section { background: #fff; border-radius: 12px; padding: 32px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,.06); }
.review-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.review-header h3 { font-size: 18px; }
.review-form { margin-bottom: 24px; padding: 16px; background: #fafafa; border-radius: 8px; }
.review-item { border-bottom: 1px solid #f0f0f0; padding: 16px 0; }
.review-user { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.review-name { font-weight: bold; font-size: 14px; }
.review-item p { color: #555; font-size: 14px; line-height: 1.6; }
.review-time { color: #bbb; font-size: 12px; }
</style>
