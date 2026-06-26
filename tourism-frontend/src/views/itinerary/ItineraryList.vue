<template>
  <div class="list-page">
    <MainHeader />
    <div class="page-content">
      <div class="page-header"><h2>📋 我的行程</h2><el-button type="primary" @click="$router.push('/itinerary/plan')">新建行程</el-button></div>
      <div v-if="list.length===0" style="text-align:center;padding:60px"><el-empty description="暂无行程"><el-button type="primary" @click="$router.push('/itinerary/plan')">去规划行程</el-button></el-empty></div>
      <div class="itin-grid">
        <el-card v-for="item in list" :key="item.id" shadow="hover" @click="$router.push(`/itinerary/${item.id}`)" class="itin-card">
          <h3>{{ item.title }}</h3>
          <p>📍 {{ item.destination }} · 🗓 {{ item.days }}天 · 💰 ¥{{ item.budget || 0 }}</p>
          <p class="prefs" v-if="item.preferences">{{ item.preferences }}</p>
          <span class="time">{{ item.createTime }}</span>
        </el-card>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'

const list = ref([])
onMounted(async () => { try { const res = await request({ url: '/itineraries' }); list.value = res.data || [] } catch {} })
</script>

<style scoped>
.list-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1000px; margin: 24px auto; padding: 0 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-header h2 { font-size: 24px; }
.itin-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
@media (max-width: 768px) { .itin-grid { grid-template-columns: repeat(2, 1fr); } }
.itin-card { cursor: pointer; transition: transform .2s; }
.itin-card:hover { transform: translateY(-2px); }
.itin-card h3 { margin-bottom: 8px; }
.itin-card p { color: #666; font-size: 14px; }
.prefs { color: #409eff; font-size: 13px; margin-top: 4px; }
.time { color: #bbb; font-size: 12px; }
</style>
