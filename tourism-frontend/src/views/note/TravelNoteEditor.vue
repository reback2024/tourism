<template>
  <div class="editor-page">
    <MainHeader />
    <div class="page-content">
      <h2>{{ isEdit ? '编辑游记' : '写游记' }}</h2>
      <el-form :model="form" label-position="top">
        <el-form-item label="标题" required><el-input v-model="form.title" placeholder="给你的游记起个标题..." /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="目的地"><el-input v-model="form.destination" placeholder="如：杭州" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="封面图URL"><el-input v-model="form.coverImage" placeholder="图片链接（选填）" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="标签"><el-input v-model="form.tags" placeholder="逗号分隔，如：美食,摄影" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="内容" required>
          <el-input v-model="form.content" type="textarea" :rows="15" placeholder="分享你的旅行故事...（支持HTML）" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleSubmit">{{ isEdit ? '保存修改' : '发布游记' }}</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'
import { ElMessage } from 'element-plus'

const route = useRoute(); const router = useRouter(); const loading = ref(false)
const isEdit = ref(false)
const form = reactive({ title: '', content: '', coverImage: '', destination: '', tags: '' })

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    try {
      const res = await request({ url: `/travel-notes/${route.params.id}` })
      Object.assign(form, res.data)
    } catch {}
  }
})

async function handleSubmit() {
  if (!form.title || !form.content) { ElMessage.warning('标题和内容不能为空'); return }
  loading.value = true
  try {
    if (isEdit.value) {
      await request({ url: `/travel-notes/${route.params.id}`, method: 'put', data: form })
      ElMessage.success('已更新')
    } else {
      await request({ url: '/travel-notes', method: 'post', data: form })
      ElMessage.success('发布成功')
    }
    router.push('/travel-notes')
  } catch {} finally { loading.value = false }
}
</script>

<style scoped>
.editor-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 900px; margin: 24px auto; padding: 40px; background: #fff; border-radius: 12px; }
.page-content h2 { margin-bottom: 24px; }
</style>
