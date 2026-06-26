<template>
  <div>
    <h3 style="margin-bottom:24px">数据概览</h3>
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card shadow="hover" style="margin-bottom:20px;text-align:center">
          <div style="font-size:36px;font-weight:bold;color:#409eff">{{ card.value }}</div>
          <div style="color:#999;margin-top:8px">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/api/request'
const cards = ref([
  { label: '用户数', value: 0 }, { label: '景点数', value: 0 }, { label: '酒店数', value: 0 },
  { label: '餐厅数', value: 0 }, { label: '评价数', value: 0 }, { label: '游记数', value: 0 }, { label: '收藏数', value: 0 }
])
onMounted(async () => {
  try {
    const res = await request({ url: '/admin/dashboard' })
    const d = res.data
    cards.value = [
      { label: '用户数', value: d.userCount }, { label: '景点', value: d.attractionCount },
      { label: '酒店', value: d.hotelCount }, { label: '餐厅', value: d.restaurantCount },
      { label: '评价', value: d.reviewCount }, { label: '游记', value: d.travelNoteCount }
    ]
  } catch {}
})
</script>
