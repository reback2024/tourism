<template>
  <div class="list-page">
    <MainHeader />
    <div class="page-content">
      <div class="page-header">
        <h2>📝 游记分享</h2>
        <el-button v-if="userStore.isLoggedIn" type="primary" @click="$router.push('/travel-notes/write')">写游记</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="搜索游记..." :prefix-icon="Search" clearable class="filter-search" @keyup.enter="loadData" />
        <el-select v-model="destination" placeholder="目的地" clearable @change="loadData"><el-option v-for="c in cities" :key="c" :label="c" :value="c" /></el-select>
        <el-select v-model="sortBy" @change="loadData">
          <el-option label="最新发布" value="create_time" /><el-option label="最多浏览" value="view_count" /><el-option label="最多点赞" value="like_count" />
        </el-select>
      </div>
      <div v-if="list.length === 0" style="text-align:center;padding:60px"><el-empty description="暂无游记" /></div>
      <div class="note-list">
        <el-card v-for="note in list" :key="note.id" shadow="hover" class="note-card" @click="$router.push(`/travel-notes/${note.id}`)">
          <div class="note-cover" v-if="note.coverImage"><img :src="note.coverImage" @error="e => e.target.style.display='none'" /></div>
          <div class="note-body">
            <h3>{{ note.title }}</h3>
            <p class="note-meta">
              <span>👤 {{ note.username }}</span>
              <span>📍 {{ note.destination }}</span>
              <span>👁 {{ note.viewCount }}</span>
              <span>❤ {{ note.likeCount }}</span>
            </p>
            <p class="note-excerpt" v-if="!note.coverImage">{{ note.content?.replace(/<[^>]*>/g, '').substring(0, 150) }}...</p>
          </div>
        </el-card>
      </div>
      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination v-model:current-page="page" :total="total" :page-size="size" layout="total, prev, pager, next" @change="loadData" />
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
import { useUserStore } from '@/stores/userStore'
import { Search } from '@element-plus/icons-vue'

const userStore = useUserStore()
const list = ref([]), total = ref(0), cities = ref([])
const page = ref(1), size = 10, keyword = ref(''), destination = ref(''), sortBy = ref('create_time')

onMounted(loadData)
async function loadData() {
  try {
    const res = await request({ url: '/travel-notes', params: { page: page.value, size, keyword: keyword.value || undefined, destination: destination.value || undefined, sortBy: sortBy.value } })
    list.value = res.data.records; total.value = res.data.total
  } catch { list.value = [] }
}
</script>

<style scoped>
.list-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1000px; margin: 24px auto; padding: 0 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 24px; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 20px; }
.filter-search { width: 280px; }
.note-list { display: flex; flex-direction: column; gap: 16px; }
.note-card { cursor: pointer; transition: transform .2s; display: flex; gap: 16px; }
.note-card:hover { transform: translateY(-2px); }
.note-cover { width: 200px; height: 140px; flex-shrink: 0; border-radius: 8px; overflow: hidden; }
.note-cover img { width: 100%; height: 100%; object-fit: cover; }
.note-body { flex: 1; padding: 8px 0; }
.note-body h3 { font-size: 18px; margin-bottom: 8px; }
.note-meta { color: #999; font-size: 13px; display: flex; gap: 16px; margin-bottom: 8px; }
.note-excerpt { color: #666; font-size: 14px; line-height: 1.6; }
.pagination-wrap { display: flex; justify-content: center; margin-top: 32px; }
</style>
