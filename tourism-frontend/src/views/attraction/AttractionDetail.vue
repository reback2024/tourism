<template>
  <div class="detail-page">
    <MainHeader />
    <div class="container" v-if="attraction">
      <div class="breadcrumb-wrap">
        <router-link to="/">首页</router-link> / <router-link to="/attractions">景点</router-link> / <span>{{ attraction.name }}</span>
      </div>

      <div class="hero-card">
        <div class="hero-img">
          <img :src="attraction.imageUrl || fallback" :alt="attraction.name" @error="e=>e.target.src=fallback" />
          <div class="img-tags">
            <span class="tag-city">{{ attraction.city }}</span>
            <span class="tag-cat">{{ attraction.category }}</span>
          </div>
        </div>
        <div class="hero-info">
          <h1>{{ attraction.name }}</h1>
          <div class="rating-row">
            <span class="big-rating">⭐ {{ attraction.rating }}</span>
            <span class="rev-count">{{ attraction.reviewCount }} 条评价</span>
          </div>
          <div class="info-rows">
            <div class="info-row" v-if="attraction.address">📍 {{ attraction.address }}</div>
            <div class="info-row" v-if="attraction.openingHours">🕐 {{ attraction.openingHours }}</div>
            <div class="info-row">
              <span v-if="attraction.ticketPrice>0" class="ticket">💰 门票 ¥{{ attraction.ticketPrice }}</span>
              <span v-else class="ticket free">🆓 免费开放</span>
            </div>
          </div>
          <div class="actions">
            <el-button size="large" round :type="isFav?'warning':'default'" @click="toggleFav">{{ isFav?'❤️ 已收藏':'🤍 收藏' }}</el-button>
            <el-button size="large" round type="primary" @click="$router.push(`/itinerary/plan?destination=${attraction.city}`)">✨ AI规划行程</el-button>
          </div>
        </div>
      </div>

      <div class="content-card">
        <h3>📖 景点介绍</h3>
        <p class="desc">{{ attraction.description }}</p>
      </div>

      <div class="review-card">
        <div class="review-head">
          <h3>💬 游客评价</h3>
          <el-button v-if="userStore.isLoggedIn" type="primary" round @click="showForm=true">写评价</el-button>
        </div>
        <div v-if="showForm" class="review-form">
          <div class="rate-row"><span>评分</span><el-rate v-model="form.rating" show-score /></div>
          <el-input v-model="form.content" type="textarea" :rows="3" placeholder="分享你的游玩体验..." />
          <el-button type="primary" round @click="submitReview" style="margin-top:12px">发表评价</el-button>
        </div>
        <div v-if="reviews.length===0" class="empty-reviews">暂无评价，来第一个评价吧 ✍️</div>
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="rev-header">
            <el-avatar :size="36">{{ r.username?.charAt(0) }}</el-avatar>
            <div>
              <div class="rev-name">{{ r.username }}</div>
              <el-rate :model-value="r.rating" disabled size="small" />
            </div>
            <span class="rev-time">{{ r.createTime }}</span>
          </div>
          <p class="rev-content">{{ r.content }}</p>
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

const route=useRoute(); const userStore=useUserStore()
const attraction=ref(null); const reviews=ref([]); const showForm=ref(false); const isFav=ref(false)
const form=reactive({rating:5,content:''})
const fallback='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAwIiBoZWlnaHQ9IjQwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9ImciIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPjxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM2NjdlYWEiLz48c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTIiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2cpIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjI0IiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iIG9wYWNpdHk9IjAuOCI+5Zu+54mH5Yqg6L295LitPC90ZXh0Pjwvc3ZnPg=='

onMounted(async()=>{
  try{const r=await getAttractionDetail(route.params.id);attraction.value=r.data}catch{}
  loadReviews()
})
async function loadReviews(){
  try{const r=await request({url:'/reviews',params:{targetType:'ATTRACTION',targetId:route.params.id}});reviews.value=r.data.records||[]}catch{}
}
async function toggleFav(){
  if(!userStore.isLoggedIn){ElMessage.warning('请先登录');return}
  try{
    if(isFav.value){await request({url:'/favorites',method:'delete',data:{targetType:'ATTRACTION',targetId:attraction.value.id}});isFav.value=false}
    else{await request({url:'/favorites',method:'post',data:{targetType:'ATTRACTION',targetId:attraction.value.id}});isFav.value=true}
  }catch{}
}
async function submitReview(){
  try{
    await request({url:'/reviews',method:'post',data:{targetType:'ATTRACTION',targetId:attraction.value.id,rating:form.rating,content:form.content}})
    ElMessage.success('评价成功');showForm.value=false;form.content='';loadReviews()
  }catch{}
}
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f0f2f5; }
.container { max-width: 1100px; margin: 0 auto; padding: 24px; }
.breadcrumb-wrap { margin-bottom: 20px; color: #999; font-size: 14px; }
.breadcrumb-wrap a { color: #667eea; }
.breadcrumb-wrap a:hover { text-decoration: underline; }

.hero-card { display: flex; gap: 32px; background: #fff; border-radius: 20px; padding: 32px; box-shadow: 0 2px 12px rgba(0,0,0,0.04); margin-bottom: 20px; }
@media (max-width: 768px) { .hero-card { flex-direction: column; } }
.hero-img { width: 480px; height: 340px; border-radius: 16px; overflow: hidden; position: relative; flex-shrink: 0; }
.hero-img img { width: 100%; height: 100%; object-fit: cover; }
.img-tags { position: absolute; top: 16px; left: 16px; display: flex; gap: 8px; }
.tag-city { background: rgba(102,126,234,0.9); color: #fff; padding: 6px 16px; border-radius: 20px; font-size: 13px; font-weight: 600; }
.tag-cat { background: rgba(255,255,255,0.9); color: #667eea; padding: 6px 16px; border-radius: 20px; font-size: 13px; }
.hero-info { flex: 1; display: flex; flex-direction: column; }
.hero-info h1 { font-size: 30px; font-weight: 800; margin-bottom: 12px; }
.rating-row { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.big-rating { font-size: 22px; font-weight: 700; color: #f59e0b; }
.rev-count { color: #999; font-size: 14px; }
.info-rows { margin-bottom: 24px; }
.info-row { padding: 8px 0; color: #666; font-size: 15px; border-bottom: 1px solid #f5f5f5; }
.ticket { color: #e6a23c; font-weight: 600; } .ticket.free { color: #67c23a; }
.actions { display: flex; gap: 12px; margin-top: auto; }

.content-card, .review-card { background: #fff; border-radius: 20px; padding: 28px 32px; box-shadow: 0 2px 12px rgba(0,0,0,0.04); margin-bottom: 20px; }
.content-card h3, .review-card h3 { font-size: 20px; margin-bottom: 16px; }
.desc { color: #555; font-size: 15px; line-height: 1.9; }
.review-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.review-form { background: #f8f9ff; border-radius: 16px; padding: 20px; margin-bottom: 20px; }
.rate-row { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; font-size: 15px; }
.empty-reviews { text-align: center; color: #ccc; padding: 40px; font-size: 15px; }
.review-item { padding: 16px 0; border-bottom: 1px solid #f0f0f0; }
.rev-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.rev-name { font-weight: 600; font-size: 14px; }
.rev-time { margin-left: auto; color: #ccc; font-size: 13px; }
.rev-content { color: #555; font-size: 14px; line-height: 1.7; padding-left: 4px; }
</style>
