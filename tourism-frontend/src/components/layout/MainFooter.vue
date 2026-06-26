<template>
  <footer class="main-footer">
    <div class="footer-grid">
      <div class="footer-col brand">
        <h3>🏔 智游</h3>
        <p>AI驱动的智能旅游推荐平台</p>
        <p class="powered">Powered by 智谱AI</p>
      </div>
      <div class="footer-col">
        <h4>探索</h4>
        <router-link to="/attractions">景点推荐</router-link>
        <router-link to="/hotels">酒店推荐</router-link>
        <router-link to="/restaurants">美食推荐</router-link>
      </div>
      <div class="footer-col">
        <h4>社区</h4>
        <router-link to="/travel-notes">游记分享</router-link>
        <router-link to="/itinerary/plan">AI行程规划</router-link>
      </div>
      <div class="footer-col">
        <h4>关于</h4>
        <p>SpringBoot + Vue3 + MyBatis Plus</p>
        <p>智谱AI大模型驱动</p>
      </div>
    </div>
    <div class="footer-bottom">© 2026 智能旅游推荐平台 · All rights reserved</div>
  </footer>
</template>

<style scoped>
.main-footer { background: #1a1a2e; color: #ccc; margin-top: 80px; }
.footer-grid {
  max-width: 1200px; margin: 0 auto; padding: 48px 24px;
  display: grid; grid-template-columns: 2fr 1fr 1fr 1fr; gap: 40px;
}
.footer-col h3 { color: #fff; font-size: 22px; margin-bottom: 12px; }
.footer-col h4 { color: #fff; font-size: 15px; margin-bottom: 16px; }
.footer-col p { font-size: 14px; line-height: 1.8; color: #999; }
.powered { margin-top: 8px; color: #667eea !important; }
.footer-col a { display: block; font-size: 14px; color: #999; margin-bottom: 10px; transition: color .2s; }
.footer-col a:hover { color: #667eea; }
.footer-bottom { border-top: 1px solid #2a2a4a; text-align: center; padding: 20px; font-size: 13px; color: #666; }
@media (max-width: 768px) { .footer-grid { grid-template-columns: 1fr 1fr; } }
</style>
