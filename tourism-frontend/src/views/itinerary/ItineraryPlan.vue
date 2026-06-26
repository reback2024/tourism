<template>
  <div class="plan-page">
    <MainHeader />
    <div class="page-content">
      <h2>🤖 AI智能行程规划</h2>
      <p class="subtitle">由智谱AI为你生成专属旅行方案</p>

      <el-card class="plan-form">
        <el-form :model="form" label-position="top">
          <el-row :gutter="20">
            <el-col :span="8"><el-form-item label="目的地"><el-input v-model="form.destination" placeholder="如：杭州" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="行程天数"><el-input-number v-model="form.days" :min="1" :max="15" /></el-form-item></el-col>
            <el-col :span="8"><el-form-item label="预算(元)"><el-input-number v-model="form.budget" :min="0" :step="500" :precision="0" /></el-form-item></el-col>
          </el-row>
          <el-form-item label="兴趣偏好">
            <el-checkbox-group v-model="form.preferences">
              <el-checkbox label="美食" /><el-checkbox label="摄影" /><el-checkbox label="历史文化" />
              <el-checkbox label="自然风光" /><el-checkbox label="购物" /><el-checkbox label="亲子" />
              <el-checkbox label="户外运动" /><el-checkbox label="休闲度假" />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleGenerate">✨ AI生成行程</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- AI生成的行程结果 -->
      <el-card v-if="result" class="result-card">
        <div class="result-header">
          <h3>📋 {{ result.destination }} {{ result.days }}日游</h3>
          <el-button type="success" @click="saveItinerary" :loading="saving">💾 保存行程</el-button>
        </div>
        <div class="result-content" v-html="renderedContent"></div>
      </el-card>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import request from '@/api/request'
import { ElMessage } from 'element-plus'

const route = useRoute(); const router = useRouter()
const loading = ref(false), saving = ref(false), result = ref(null)
const form = reactive({
  destination: route.query.destination || '',
  days: 3, budget: 3000,
  preferences: []
})

const renderedContent = computed(() => {
  if (!result.value?.content) return ''
  // 简单Markdown转HTML
  return result.value.content
    .replace(/### (.*)/g, '<h3>$1</h3>')
    .replace(/## (.*)/g, '<h2>$1</h2>')
    .replace(/\*\*(.*?)\*\*/g, '<b>$1</b>')
    .replace(/\n/g, '<br>')
})

async function handleGenerate() {
  if (!form.destination) { ElMessage.warning('请输入目的地'); return }
  loading.value = true
  try {
    const prefs = form.preferences.join('、')
    const res = await request({
      url: '/ai/generate-itinerary', method: 'post',
      data: { destination: form.destination, days: form.days, budget: form.budget, preferences: prefs }
    })
    result.value = res.data
    ElMessage.success('AI行程已生成！')
  } catch {} finally { loading.value = false }
}

async function saveItinerary() {
  saving.value = true
  try {
    await request({ url: '/itineraries', method: 'post', data: {
      title: `${result.value.destination}${result.value.days}日游`,
      destination: result.value.destination,
      days: parseInt(result.value.days),
      budget: parseInt(result.value.budget),
      preferences: result.value.preferences,
      content: result.value.content
    }})
    ElMessage.success('行程已保存！')
    router.push('/itinerary/list')
  } catch {} finally { saving.value = false }
}
</script>

<style scoped>
.plan-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1000px; margin: 24px auto; padding: 0 20px; }
.page-content h2 { font-size: 24px; margin-bottom: 8px; }
.subtitle { color: #999; margin-bottom: 24px; }
.plan-form { margin-bottom: 24px; }
.result-card { margin-top: 24px; }
.result-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.result-content { line-height: 1.9; font-size: 15px; color: #333; }
.result-content :deep(h2) { font-size: 20px; margin: 16px 0 8px; }
.result-content :deep(h3) { font-size: 17px; margin: 12px 0 6px; color: #409eff; }
.result-content :deep(b) { color: #e6a23c; }
</style>
