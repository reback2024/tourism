<template>
  <div class="detail-page">
    <MainHeader />
    <div class="page-content" v-if="itinerary">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item :to="{ path: '/itinerary/list' }">我的行程</el-breadcrumb-item><el-breadcrumb-item>{{ itinerary.title }}</el-breadcrumb-item></el-breadcrumb>
      <div class="detail-card">
        <div class="header">
          <h1>{{ itinerary.title }}</h1>
          <div class="meta">📍 {{ itinerary.destination }} · 🗓 {{ itinerary.days }}天 · 💰 ¥{{ itinerary.budget || 0 }}</div>
          <div class="prefs" v-if="itinerary.preferences">🏷 {{ itinerary.preferences }}</div>
        </div>
        <div class="content" v-html="itinerary.content" v-if="itinerary.content"></div>
        <div class="days" v-if="itinerary.daysDetail">
          <el-timeline>
            <el-timeline-item v-for="day in itinerary.daysDetail" :key="day.dayNumber" :timestamp="`第 ${day.dayNumber} 天`" placement="top">
              <el-card><p v-if="day.attractions">🏛 {{ day.attractions }}</p><p v-if="day.restaurants">🍜 {{ day.restaurants }}</p><p v-if="day.hotels">🏨 {{ day.hotels }}</p><p v-if="day.notes">📝 {{ day.notes }}</p></el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'

const route = useRoute(); const itinerary = ref(null)
onMounted(async () => { try { const res = await request({ url: `/itineraries/${route.params.id}` }); itinerary.value = res.data } catch {} })
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 900px; margin: 24px auto; padding: 0 20px; }
.detail-card { background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 2px 12px rgba(0,0,0,.06); }
.header { margin-bottom: 24px; }
.header h1 { font-size: 28px; margin-bottom: 8px; }
.meta { color: #666; font-size: 15px; }
.prefs { color: #409eff; margin-top: 8px; }
.content { line-height: 1.8; font-size: 15px; margin-bottom: 32px; }
</style>
