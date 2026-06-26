<template>
  <div class="register-page">
    <div class="visual-panel">
      <div class="visual-bg"><div class="floating-icons">
        <span class="fi fi1">✈</span><span class="fi fi2">🌍</span><span class="fi fi3">🎒</span><span class="fi fi4">🗺</span><span class="fi fi5">📸</span><span class="fi fi6">🌟</span>
      </div></div>
      <div class="visual-content">
        <h1>智游</h1>
        <p class="subtitle">加入我们，开启智能旅行新体验</p>
        <div class="feature-list">
          <div class="fl-item">✨ 免费注册，即刻使用</div>
          <div class="fl-item">🤖 AI为你规划专属行程</div>
          <div class="fl-item">💬 随时随地AI旅游助手</div>
        </div>
      </div>
    </div>

    <div class="form-panel">
      <div class="form-card">
        <div class="form-header">
          <span class="welcome-emoji">🚀</span>
          <h2>创建账号</h2>
          <p>开启你的智能旅行</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleRegister">
          <div class="input-group"><span class="input-icon">👤</span><input v-model="form.username" placeholder="用户名（3-50位）" class="custom-input" /></div>
          <div class="input-group"><span class="input-icon">✏</span><input v-model="form.nickname" placeholder="昵称（选填）" class="custom-input" /></div>
          <div class="input-group"><span class="input-icon">🔒</span><input v-model="form.password" type="password" placeholder="密码（至少6位）" class="custom-input" /></div>
          <div class="input-group"><span class="input-icon">🔐</span><input v-model="form.confirmPassword" type="password" placeholder="确认密码" class="custom-input" /></div>

          <el-button type="primary" size="large" :loading="loading" class="reg-btn" @click="handleRegister">注 册</el-button>
        </el-form>

        <div class="form-footer">已有账号？<router-link to="/login">立即登录 →</router-link></div>
        <router-link to="/" class="go-home">← 返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter(); const userStore = useUserStore()
const formRef = ref(); const loading = ref(false)
const form = reactive({ username: '', nickname: '', password: '', confirmPassword: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名' }, { min: 3, max: 50, message: '长度3-50位' }],
  password: [{ required: true, message: '请输入密码' }, { min: 6, message: '至少6位' }],
  confirmPassword: [
    { required: true, message: '请确认密码' },
    { validator: (_, v, cb) => v !== form.password ? cb(new Error('两次密码不一致')) : cb(), trigger: 'blur' }
  ]
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.register({ username: form.username, password: form.password, nickname: form.nickname || undefined })
    ElMessage.success('注册成功！请登录')
    router.push('/login')
  } catch {} finally { loading.value = false }
}
</script>

<style scoped>
.register-page { display: flex; min-height: 100vh; }

.visual-panel {
  flex: 1; position: relative; overflow: hidden; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}
.visual-bg { position: absolute; inset: 0; }
.visual-bg::before {
  content: ''; position: absolute; inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
.floating-icons { position: absolute; inset: 0; }
.fi { position: absolute; opacity: 0.15; animation: float 6s ease-in-out infinite; }
.fi1 { top: 10%; left: 15%; font-size: 60px; animation-delay: 0s; }
.fi2 { top: 30%; right: 10%; font-size: 45px; animation-delay: -1.5s; }
.fi3 { top: 55%; left: 10%; font-size: 50px; animation-delay: -3s; }
.fi4 { bottom: 20%; right: 15%; font-size: 55px; animation-delay: -2s; }
.fi5 { top: 15%; right: 40%; font-size: 40px; animation-delay: -4s; }
.fi6 { bottom: 35%; left: 40%; font-size: 48px; animation-delay: -5s; }
@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  33% { transform: translateY(-20px) rotate(5deg); }
  66% { transform: translateY(10px) rotate(-3deg); }
}
.visual-content { position: relative; z-index: 1; text-align: center; color: #fff; padding: 40px; }
.visual-content h1 { font-size: 64px; font-weight: 900; margin-bottom: 12px; text-shadow: 0 4px 30px rgba(0,0,0,.2); }
.subtitle { font-size: 18px; opacity: 0.85; margin-bottom: 40px; }
.feature-list { text-align: left; display: inline-block; }
.fl-item { font-size: 16px; padding: 10px 0; opacity: 0.8; transition: all .3s; }
.fl-item:hover { opacity: 1; transform: translateX(8px); }

.form-panel { width: 520px; display: flex; align-items: center; justify-content: center; background: #fff; padding: 60px; }
.form-card { width: 100%; max-width: 400px; }
.form-header { text-align: center; margin-bottom: 28px; }
.welcome-emoji { font-size: 48px; display: block; margin-bottom: 8px; animation: bounce 1.5s infinite; }
@keyframes bounce { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-10px)} }
.form-header h2 { font-size: 28px; font-weight: 800; margin-bottom: 6px; color: #1a1a2e; }
.form-header p { color: #999; font-size: 15px; }

.input-group {
  display: flex; align-items: center; gap: 12px; padding: 14px 18px;
  background: #f5f6f8; border-radius: 14px; margin-bottom: 14px;
  border: 2px solid transparent; transition: all .3s;
}
.input-group:focus-within { background: #fff; border-color: #11998e; box-shadow: 0 0 0 4px rgba(17,153,142,.08); }
.input-icon { font-size: 20px; flex-shrink: 0; }
.custom-input { flex: 1; border: none; outline: none; background: transparent; font-size: 15px; color: #333; }
.custom-input::placeholder { color: #ccc; }

.reg-btn {
  width: 100%; height: 52px; font-size: 18px; font-weight: 600; letter-spacing: 4px;
  border-radius: 14px !important;
  background: linear-gradient(135deg, #11998e, #38ef7d) !important;
  border: none !important; margin-top: 4px; color: #fff !important;
  transition: all .3s !important;
}
.reg-btn:hover { transform: translateY(-2px); box-shadow: 0 8px 25px rgba(17,153,142,.4) !important; }

.form-footer { text-align: center; margin-top: 20px; font-size: 15px; color: #888; }
.form-footer a { color: #11998e; font-weight: 600; }
.go-home { display: block; text-align: center; margin-top: 14px; color: #ccc; font-size: 14px; }

@media (max-width: 900px) { .visual-panel { display: none; } .form-panel { width: 100%; } }
</style>
