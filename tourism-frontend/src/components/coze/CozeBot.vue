<template>
  <div class="coze-bot-wrapper">
    <div v-show="!visible" class="coze-fab" @click="openBot">
      <el-icon :size="24"><ChatDotRound /></el-icon>
    </div>
    <transition name="slide">
      <div v-show="visible" class="coze-chat">
        <div class="coze-header">
          <span>🤖 AI旅游助手</span>
          <el-icon class="close-btn" @click="closeBot"><Close /></el-icon>
        </div>
        <div class="coze-body" ref="chatBody">
          <div v-for="(msg, i) in messages" :key="i" :class="['msg', msg.role]">
            <div class="msg-avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
            <div class="msg-content" v-text="msg.content"></div>
          </div>
          <div v-if="loading" class="msg assistant">
            <div class="msg-avatar">🤖</div>
            <div class="msg-content typing">思考中...</div>
          </div>
        </div>
        <div class="coze-input">
          <el-input v-model="input" placeholder="问我旅游相关问题..." @keyup.enter="sendMsg" :disabled="loading">
            <template #append><el-button :icon="Promotion" @click="sendMsg" :loading="loading" /></template>
          </el-input>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import request from '@/api/request'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const visible = ref(false), input = ref(''), loading = ref(false), chatBody = ref(null)
const messages = ref([
  { role: 'assistant', content: '👋 你好！我是AI旅游助手，可以帮你推荐景点、规划行程、介绍美食，随时问我！' }
])

function openBot() { visible.value = true; scrollBottom() }
function closeBot() { visible.value = false }

async function sendMsg() {
  const text = input.value.trim()
  if (!text || loading.value) return
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录后再使用AI助手'); return }

  messages.value.push({ role: 'user', content: text })
  input.value = ''
  loading.value = true
  await scrollBottom()

  try {
    const history = messages.value.slice(0, -1).map(m => ({ role: m.role, content: m.content }))
    const res = await request({ url: '/ai/chat', method: 'post', data: { message: text, history } })
    messages.value.push({ role: 'assistant', content: res.data.reply })
  } catch {
    messages.value.push({ role: 'assistant', content: '网络异常，请稍后重试' })
  } finally {
    loading.value = false
    await scrollBottom()
  }
}

async function scrollBottom() {
  await nextTick()
  if (chatBody.value) chatBody.value.scrollTop = chatBody.value.scrollHeight
}
</script>

<style scoped>
.coze-bot-wrapper { position: fixed; z-index: 9999; }
.coze-fab {
  position: fixed; bottom: 24px; right: 24px;
  width: 56px; height: 56px; border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff; display: flex; align-items: center; justify-content: center;
  cursor: pointer; box-shadow: 0 4px 16px rgba(102,126,234,.4); transition: transform .2s;
}
.coze-fab:hover { transform: scale(1.1); }
.coze-chat {
  position: fixed; bottom: 90px; right: 24px;
  width: 420px; height: 540px; background: #fff;
  border-radius: 16px; box-shadow: 0 8px 32px rgba(0,0,0,.15);
  display: flex; flex-direction: column; overflow: hidden;
}
.coze-header {
  background: linear-gradient(135deg, #667eea, #764ba2); color: #fff;
  padding: 14px 20px; display: flex; justify-content: space-between; align-items: center;
}
.close-btn { cursor: pointer; }
.coze-body { flex: 1; overflow-y: auto; padding: 16px; }
.msg { display: flex; gap: 8px; margin-bottom: 16px; }
.msg.user { flex-direction: row-reverse; }
.msg-avatar { font-size: 24px; flex-shrink: 0; }
.msg-content {
  max-width: 75%; padding: 10px 14px; border-radius: 12px;
  font-size: 14px; line-height: 1.6; white-space: pre-wrap;
}
.msg.user .msg-content { background: #409eff; color: #fff; }
.msg.assistant .msg-content { background: #f0f0f0; color: #333; }
.typing { color: #999; font-style: italic; }
.coze-input { padding: 12px; border-top: 1px solid #eee; }
.slide-enter-active, .slide-leave-active { transition: all .3s ease; }
.slide-enter-from, .slide-leave-to { opacity: 0; transform: translateY(20px); }
</style>
