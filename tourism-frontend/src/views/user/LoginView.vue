<template>
  <div class="login-page">
    <!-- 左侧视觉区 -->
    <div class="visual-panel">
      <div class="visual-bg">
        <div class="floating-icons">
          <span class="fi fi1">🏔</span>
          <span class="fi fi2">🏛</span>
          <span class="fi fi3">🌊</span>
          <span class="fi fi4">🏯</span>
          <span class="fi fi5">🎡</span>
          <span class="fi fi6">🗿</span>
          <span class="fi fi7">🌴</span>
          <span class="fi fi8">⛩</span>
        </div>
      </div>
      <div class="visual-content">
        <h1>智游</h1>
        <p class="subtitle">AI驱动的智能旅游推荐平台</p>
        <div class="feature-list">
          <div class="fl-item">🤖 AI智能行程规划</div>
          <div class="fl-item">🏛 海量景点推荐</div>
          <div class="fl-item">💬 AI旅游助手</div>
          <div class="fl-item">📝 游记分享社区</div>
        </div>
      </div>
    </div>

    <!-- 右侧登录区 -->
    <div class="form-panel">
      <div class="form-card">
        <div class="form-header">
          <span class="welcome-emoji">👋</span>
          <h2>欢迎回来</h2>
          <p>登录你的账号，继续探索世界</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleLogin">
          <div class="input-group">
            <span class="input-icon">👤</span>
            <input v-model="form.username" placeholder="用户名" class="custom-input" />
          </div>
          <div class="input-group">
            <span class="input-icon">🔒</span>
            <input v-model="form.password" type="password" placeholder="密码" class="custom-input" @keyup.enter="handleLogin" />
          </div>

          <el-button type="primary" size="large" :loading="loading" class="login-btn" @click="handleLogin">
            登 录
          </el-button>
        </el-form>

        <div class="form-footer">
          还没有账号？<router-link to="/register">立即注册 →</router-link>
        </div>
        <router-link to="/" class="go-home">← 返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter(); const route = useRoute(); const userStore = useUserStore()
const formRef = ref(); const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功！')
    router.push(route.query.redirect || '/')
  } catch {} finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  display: flex; min-height: 100vh;
}

/* ===== 左侧视觉区 ===== */
.visual-panel {
  flex: 1; position: relative; overflow: hidden; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
}
.visual-bg {
  position: absolute; inset: 0; overflow: hidden;
}
.visual-bg::before {
  content: ''; position: absolute; inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
.floating-icons { position: absolute; inset: 0; }
.fi {
  position: absolute; font-size: 40px; opacity: 0.15;
  animation: float 6s ease-in-out infinite;
  filter: drop-shadow(0 0 10px rgba(255,255,255,0.3));
}
.fi1 { top: 10%; left: 10%; animation-delay: 0s; font-size: 50px; }
.fi2 { top: 25%; right: 15%; animation-delay: -1s; font-size: 35px; }
.fi3 { top: 50%; left: 8%; animation-delay: -2s; font-size: 45px; }
.fi4 { top: 15%; left: 55%; animation-delay: -3s; font-size: 38px; }
.fi5 { bottom: 20%; right: 10%; animation-delay: -4s; font-size: 42px; }
.fi6 { bottom: 40%; left: 20%; animation-delay: -5s; font-size: 55px; }
.fi7 { top: 60%; right: 25%; animation-delay: -2.5s; font-size: 32px; }
.fi8 { top: 40%; left: 40%; animation-delay: -1.5s; font-size: 48px; }

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  33% { transform: translateY(-20px) rotate(5deg); }
  66% { transform: translateY(10px) rotate(-3deg); }
}

.visual-content {
  position: relative; z-index: 1; text-align: center; color: #fff; padding: 40px;
}
.visual-content h1 {
  font-size: 64px; font-weight: 900; margin-bottom: 12px;
  text-shadow: 0 4px 30px rgba(0,0,0,0.2);
}
.subtitle { font-size: 18px; opacity: 0.85; margin-bottom: 40px; }
.feature-list { text-align: left; display: inline-block; }
.fl-item {
  font-size: 16px; padding: 10px 0; opacity: 0.8;
  transition: all 0.3s;
}
.fl-item:hover { opacity: 1; transform: translateX(8px); }

/* ===== 右侧登录区 ===== */
.form-panel {
  width: 520px; display: flex; align-items: center; justify-content: center;
  background: #fff; padding: 60px;
}
.form-card { width: 100%; max-width: 400px; }
.form-header { text-align: center; margin-bottom: 36px; }
.welcome-emoji { font-size: 52px; display: block; margin-bottom: 12px; animation: bounce 1.5s infinite; }
@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
.form-header h2 { font-size: 28px; font-weight: 800; margin-bottom: 8px; color: #1a1a2e; }
.form-header p { color: #999; font-size: 15px; }

.input-group {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 18px; background: #f5f6f8; border-radius: 14px;
  margin-bottom: 16px; border: 2px solid transparent;
  transition: all 0.3s;
}
.input-group:focus-within { background: #fff; border-color: #667eea; box-shadow: 0 0 0 4px rgba(102,126,234,0.08); }
.input-icon { font-size: 20px; flex-shrink: 0; }
.custom-input { flex: 1; border: none; outline: none; background: transparent; font-size: 15px; color: #333; }
.custom-input::placeholder { color: #ccc; }

.login-btn {
  width: 100%; height: 52px; font-size: 18px; font-weight: 600; letter-spacing: 4px;
  border-radius: 14px !important;
  background: linear-gradient(135deg, #667eea, #764ba2) !important;
  border: none !important; margin-top: 8px;
  transition: all 0.3s !important;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102,126,234,0.4) !important;
}

.form-footer { text-align: center; margin-top: 24px; font-size: 15px; color: #888; }
.form-footer a { color: #667eea; font-weight: 600; }
.form-footer a:hover { text-decoration: underline; }
.go-home { display: block; text-align: center; margin-top: 16px; color: #ccc; font-size: 14px; }

@media (max-width: 900px) { .visual-panel { display: none; } .form-panel { width: 100%; } }
</style>
