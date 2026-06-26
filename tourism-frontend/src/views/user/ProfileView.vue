<template>
  <div class="profile-page">
    <MainHeader />
    <div class="page-content">
      <el-card class="profile-card">
        <div class="user-info">
          <el-avatar :size="80" :src="userStore.user?.avatar"><el-icon :size="40"><UserFilled /></el-icon></el-avatar>
          <div class="user-detail">
            <h2>{{ userStore.user?.nickname || userStore.user?.username }}</h2>
            <p>用户名: {{ userStore.user?.username }} | 角色: {{ userStore.user?.role === 'ADMIN' ? '管理员' : '普通用户' }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="section-card">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="我的评价" name="reviews">
            <div v-if="reviews.length === 0" style="text-align:center;color:#999;padding:40px">暂无评价</div>
            <div v-for="r in reviews" :key="r.id" class="review-item">
              <div class="review-head">
                <span><b>{{ r.targetType === 'ATTRACTION' ? '景点' : r.targetType === 'HOTEL' ? '酒店' : '餐厅' }}</b></span>
                <el-rate :model-value="r.rating" disabled size="small" />
              </div>
              <p>{{ r.content }}</p>
              <span class="time">{{ r.createTime }}</span>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的收藏" name="favorites">
            <div v-if="favs.length === 0" style="text-align:center;color:#999;padding:40px">暂无收藏</div>
            <div v-for="f in favs" :key="f.id" class="fav-item" @click="goDetail(f)">
              <span><b>{{ f.name }}</b> | {{ f.city || '' }} | ⭐{{ f.rating }}</span>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
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
import { useUserStore } from '@/stores/userStore'

const router = useRouter(); const userStore = useUserStore()
const activeTab = ref('reviews'); const reviews = ref([]); const favs = ref([])

onMounted(async () => {
  try { const res = await request({ url: '/reviews/my' }); reviews.value = res.data.records || [] } catch {}
  try { const res = await request({ url: '/favorites' }); favs.value = res.data.records || [] } catch {}
})

function goDetail(f) { router.push(`/attractions/${f.id}`) }
</script>

<style scoped>
.profile-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 900px; margin: 24px auto; padding: 0 20px; }
.profile-card { margin-bottom: 20px; }
.user-info { display: flex; align-items: center; gap: 24px; }
.user-detail h2 { margin-bottom: 4px; }
.user-detail p { color: #999; }
.section-card { min-height: 300px; }
.review-item { border-bottom: 1px solid #f0f0f0; padding: 14px 0; }
.review-head { display: flex; align-items: center; gap: 12px; margin-bottom: 6px; }
.review-item p { color: #555; font-size: 14px; }
.time { color: #bbb; font-size: 12px; }
.fav-item { padding: 12px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.fav-item:hover { color: #409eff; }
</style>
