<template>
  <div class="detail-page">
    <MainHeader />
    <div class="page-content" v-if="note">
      <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item><el-breadcrumb-item :to="{ path: '/travel-notes' }">游记</el-breadcrumb-item><el-breadcrumb-item>{{ note.title }}</el-breadcrumb-item></el-breadcrumb>
      <article class="note-article">
        <h1>{{ note.title }}</h1>
        <div class="note-meta">
          <span>👤 {{ note.username }}</span>
          <span>📍 {{ note.destination }}</span>
          <span>📅 {{ note.createTime }}</span>
          <span>👁 {{ note.viewCount }} 浏览</span>
          <span>❤ {{ note.likeCount }} 点赞</span>
        </div>
        <div class="tags" v-if="note.tags">
          <el-tag v-for="t in note.tags.split(',')" :key="t" size="small">{{ t.trim() }}</el-tag>
        </div>
        <div class="note-content" v-html="note.content"></div>
        <div class="note-actions" v-if="userStore.user?.id === note.userId">
          <el-button @click="$router.push(`/travel-notes/${note.id}/edit`)">编辑</el-button>
          <el-button type="danger" @click="handleDelete">删除</el-button>
        </div>
      </article>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'
import { useUserStore } from '@/stores/userStore'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute(); const router = useRouter(); const userStore = useUserStore(); const note = ref(null)

onMounted(async () => {
  try { const res = await request({ url: `/travel-notes/${route.params.id}` }); note.value = res.data } catch {}
})

async function handleDelete() {
  try {
    await ElMessageBox.confirm('确定删除这篇游记吗？', '提示', { type: 'warning' })
    await request({ url: `/travel-notes/${route.params.id}`, method: 'delete' })
    ElMessage.success('已删除'); router.push('/travel-notes')
  } catch {}
}
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 900px; margin: 24px auto; padding: 0 20px; }
.note-article { background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 2px 12px rgba(0,0,0,.06); }
.note-article h1 { font-size: 28px; margin-bottom: 16px; }
.note-meta { color: #999; font-size: 14px; display: flex; gap: 20px; margin-bottom: 16px; flex-wrap: wrap; }
.tags { display: flex; gap: 8px; margin-bottom: 24px; }
.note-content { line-height: 1.9; font-size: 16px; color: #333; }
.note-content :deep(img) { max-width: 100%; border-radius: 8px; }
.note-actions { margin-top: 32px; padding-top: 24px; border-top: 1px solid #eee; }
</style>
