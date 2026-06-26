<template>
  <footer class="main-footer">
    <div class="footer-content">
      <div class="footer-section">
        <h4>智能旅游推荐平台</h4>
        <p>AI驱动的个性化旅游推荐平台，为您提供智能化、全方位的旅行规划服务。</p>
      </div>
      <div class="footer-section">
        <h4>快速链接</h4>
        <router-link to="/attractions">景点推荐</router-link>
        <router-link to="/hotels">酒店推荐</router-link>
        <router-link to="/restaurants">美食推荐</router-link>
        <router-link to="/travel-notes">游记分享</router-link>
      </div>
      <div class="footer-section">
        <h4>特色功能</h4>
        <router-link to="/itinerary/plan">AI行程规划</router-link>
        <router-link to="/user/favorites">我的收藏</router-link>
      </div>
      <div class="footer-section">
        <h4>联系我们</h4>
        <p>📧 support@smarttourism.com</p>
        <p>📞 400-888-0000</p>
      </div>
    </div>
    <div class="footer-bottom">
      <p>© 2026 智能旅游推荐平台. All rights reserved. Powered by Coze AI</p>
    </div>
  </footer>
</template>

<style scoped>
.main-footer {
  background: #2c3e50;
  color: #ecf0f1;
  margin-top: 60px;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 40px;
}

.footer-section h4 {
  color: #fff;
  margin-bottom: 16px;
  font-size: 16px;
}

.footer-section p {
  color: #bdc3c7;
  font-size: 14px;
  line-height: 1.8;
}

.footer-section a {
  display: block;
  color: #bdc3c7;
  font-size: 14px;
  margin-bottom: 8px;
  transition: color 0.2s;
}

.footer-section a:hover {
  color: #409eff;
}

.footer-bottom {
  border-top: 1px solid #3d566e;
  text-align: center;
  padding: 20px;
  color: #95a5a6;
  font-size: 13px;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr 1fr;
  }
}
</style>
