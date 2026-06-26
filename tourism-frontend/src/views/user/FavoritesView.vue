<template>
  <div class="fav-page">
    <MainHeader />
    <div class="page-content">
      <h2>⭐ 我的收藏</h2>
      <el-tabs v-model="tab" @tab-change="loadFavs">
        <el-tab-pane label="景点" name="ATTRACTION" />
        <el-tab-pane label="酒店" name="HOTEL" />
        <el-tab-pane label="餐厅" name="RESTAURANT" />
      </el-tabs>
      <div v-if="list.length === 0" style="text-align:center;color:#999;padding:60px">
        <el-empty description="暂无收藏" />
      </div>
      <div class="fav-grid">
        <el-card v-for="item in list" :key="item.id" shadow="hover" @click="goDetail(item)">
          <img v-if="item.imageUrl" :src="item.imageUrl" @error="e => e.target.style.display='none'" />
          <h4>{{ item.name }}</h4>
          <p>{{ item.city }} · ⭐{{ item.rating }}</p>
        </el-card>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'

const router = useRouter(); const list = ref([]); const tab = ref('ATTRACTION')

onMounted(loadFavs)

async function loadFavs() {
  try {
    const res = await request({ url: '/favorites', params: { targetType: tab.value, size: 50 } })
    list.value = res.data.records || []
  } catch { list.value = [] }
}

function goDetail(item) {
  const path = tab.value === 'ATTRACTION' ? '/attractions/' : tab.value === 'HOTEL' ? '/hotels/' : '/restaurants/'
  router.push(path + item.id)
}
</script>

<style scoped>
.fav-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1200px; margin: 24px auto; padding: 0 20px; }
.page-content h2 { margin-bottom: 20px; }
.fav-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
@media (max-width: 900px) { .fav-grid { grid-template-columns: repeat(2, 1fr); } }
.fav-grid .el-card { cursor: pointer; }
.fav-grid img { width: 100%; height: 140px; object-fit: cover; border-radius: 6px; margin-bottom: 8px; }
</style>
