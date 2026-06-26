<template>
  <div class="home">
    <MainHeader />

    <section class="hero">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <span class="hero-badge">🤖 智谱AI驱动</span>
        <h1>探索你的下一段旅程</h1>
        <p>AI 智能规划 + 海量目的地 + 真实游客评价，让每一次出行都充满惊喜</p>
        <div class="hero-search">
          <el-icon :size="22"><Search /></el-icon>
          <input v-model="kw" placeholder="搜索城市、景点、美食..." @keyup.enter="doSearch" />
          <el-button type="primary" size="large" round @click="doSearch">探索</el-button>
        </div>
        <div class="hero-stats">
          <div class="stat"><b>12+</b><span>热门城市</span></div>
          <div class="stat"><b>30+</b><span>精选地点</span></div>
          <div class="stat"><b>AI</b><span>智能规划</span></div>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="section-head">
        <div><span class="section-icon">🔥</span><h2>热门景点</h2></div>
        <router-link to="/attractions" class="more">查看全部 →</router-link>
      </div>
      <div class="card-grid cols-5">
        <div v-for="item in attractions" :key="item.id" class="spot-card" @click="$router.push(`/attractions/${item.id}`)">
          <div class="card-img">
            <img :src="item.imageUrl || fallback" :alt="item.name" @error="e=>e.target.src=fallback" />
            <div class="card-overlay">
              <span class="card-city">{{ item.city }}</span>
              <span class="card-cat">{{ item.category }}</span>
            </div>
          </div>
          <div class="card-body">
            <h3>{{ item.name }}</h3>
            <div class="card-meta">
              <span class="stars">⭐ {{ item.rating }}</span>
              <span class="price" v-if="item.ticketPrice>0">¥{{ item.ticketPrice }}</span>
              <span class="price free" v-else>免费</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="section-head">
        <div><span class="section-icon">✨</span><h2>平台特色</h2></div>
      </div>
      <div class="features">
        <div class="feat-card" v-for="f in features" :key="f.title" :style="{ '--c': f.color }">
          <div class="feat-icon">{{ f.icon }}</div>
          <h3>{{ f.title }}</h3>
          <p>{{ f.desc }}</p>
        </div>
      </div>
    </section>

    <section class="cta">
      <h2>准备好出发了吗？</h2>
      <p>让 AI 为你量身定制完美旅行计划</p>
      <el-button type="warning" size="large" round @click="$router.push('/itinerary/plan')">✨ 免费规划行程</el-button>
    </section>

    <MainFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getHotAttractions } from '@/api/attraction'

const router = useRouter(); const kw = ref(''); const attractions = ref([])
const fallback = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjI4MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9ImciIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPjxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM2NjdlYWEiLz48c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTIiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2cpIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjIwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iIG9wYWNpdHk9IjAuOCI+5Zu+54mH5Yqg6L295LitPC90ZXh0Pjwvc3ZnPg=='

const features = [
  { icon:'🤖', title:'AI行程规划', desc:'智谱大模型为你生成专属旅行方案', color:'#667eea' },
  { icon:'🏛', title:'海量目的地', desc:'覆盖全国热门城市与景点信息', color:'#67c23a' },
  { icon:'⭐', title:'真实评价', desc:'来自真实游客的评分与游记分享', color:'#e6a23c' },
  { icon:'💬', title:'AI旅游助手', desc:'随时随地与 AI 对话获取旅行建议', color:'#f56c6c' },
]

onMounted(async () => { try { const r=await getHotAttractions(); attractions.value=r.data||[] } catch {} })
function doSearch() { if(kw.value.trim()) router.push({path:'/attractions',query:{keyword:kw.value.trim()}}) }
</script>

<style scoped>
.home { background: #f0f2f5; }

/* Hero */
.hero { position: relative; padding: 80px 20px 60px; text-align: center; overflow: hidden; }
.hero-bg {
  position: absolute; inset: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  opacity: 0.06;
}
.hero-bg::after {
  content: ''; position: absolute; top: -50%; left: -50%; width: 200%; height: 200%;
  background: radial-gradient(circle at 30% 50%, rgba(102,126,234,0.1) 0%, transparent 50%),
              radial-gradient(circle at 70% 50%, rgba(240,147,251,0.1) 0%, transparent 50%);
  animation: rotate 20s linear infinite;
}
@keyframes rotate { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
.hero-content { position: relative; z-index: 1; max-width: 700px; margin: 0 auto; }
.hero-badge {
  display: inline-block; padding: 6px 16px; background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff; border-radius: 20px; font-size: 13px; margin-bottom: 20px;
}
.hero h1 { font-size: 48px; font-weight: 800; background: linear-gradient(135deg, #2c3e50, #667eea); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 16px; }
.hero p { font-size: 18px; color: #666; margin-bottom: 36px; }
.hero-search {
  display: flex; align-items: center; gap: 12px; max-width: 560px; margin: 0 auto;
  background: #fff; padding: 8px 8px 8px 20px; border-radius: 60px;
  box-shadow: 0 8px 30px rgba(102,126,234,0.15);
}
.hero-search input { flex: 1; border: none; outline: none; font-size: 16px; color: #333; }
.hero-stats { display: flex; justify-content: center; gap: 60px; margin-top: 40px; }
.stat { text-align: center; }
.stat b { display: block; font-size: 28px; font-weight: 800; color: #667eea; }
.stat span { font-size: 13px; color: #999; }

/* Section */
.section { max-width: 1400px; margin: 0 auto; padding: 60px 24px; }
.section-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 28px; }
.section-head > div { display: flex; align-items: center; gap: 10px; }
.section-icon { font-size: 24px; }
.section-head h2 { font-size: 24px; font-weight: 700; }
.more { color: #667eea; font-weight: 500; font-size: 15px; }
.more:hover { text-decoration: underline; }

/* Card Grid */
.card-grid { display: grid; gap: 20px; }
.cols-5 { grid-template-columns: repeat(5, 1fr); }
@media (max-width: 1200px) { .cols-5 { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .cols-5 { grid-template-columns: repeat(2, 1fr); } }

.spot-card {
  background: #fff; border-radius: 16px; overflow: hidden; cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04); transition: all 0.3s;
}
.spot-card:hover { transform: translateY(-6px); box-shadow: 0 12px 30px rgba(0,0,0,0.1); }
.card-img { position: relative; height: 200px; overflow: hidden; }
.card-img img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s; }
.spot-card:hover .card-img img { transform: scale(1.08); }
.card-overlay {
  position: absolute; top: 12px; left: 12px; right: 12px;
  display: flex; justify-content: space-between;
}
.card-city {
  background: rgba(102,126,234,0.9); color: #fff; padding: 4px 12px;
  border-radius: 20px; font-size: 12px; font-weight: 600;
  backdrop-filter: blur(4px);
}
.card-cat {
  background: rgba(255,255,255,0.9); color: #667eea; padding: 4px 12px;
  border-radius: 20px; font-size: 12px; backdrop-filter: blur(4px);
}
.card-body { padding: 16px; }
.card-body h3 { font-size: 16px; font-weight: 600; margin-bottom: 8px; }
.card-meta { display: flex; align-items: center; gap: 12px; }
.stars { color: #f59e0b; font-weight: 600; font-size: 14px; }
.price { color: #e6a23c; font-weight: 700; font-size: 16px; margin-left: auto; }
.price.free { color: #67c23a; font-size: 14px; }

/* Features */
.features { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
@media (max-width: 768px) { .features { grid-template-columns: repeat(2, 1fr); } }
.feat-card {
  background: #fff; border-radius: 16px; padding: 32px 24px; text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04); transition: all 0.3s;
  border-top: 3px solid var(--c);
}
.feat-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.08); }
.feat-icon { font-size: 40px; margin-bottom: 12px; }
.feat-card h3 { font-size: 17px; font-weight: 600; margin-bottom: 8px; }
.feat-card p { color: #888; font-size: 14px; line-height: 1.6; }

/* CTA */
.cta {
  text-align: center; padding: 80px 20px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff; margin: 0 24px 40px; border-radius: 24px;
}
.cta h2 { font-size: 32px; margin-bottom: 8px; }
.cta p { font-size: 16px; opacity: 0.85; margin-bottom: 24px; }
</style>
