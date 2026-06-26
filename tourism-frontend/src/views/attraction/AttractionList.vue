<template>
  <div class="list-page">
    <MainHeader />
    <div class="container">
      <div class="page-header">
        <h1>🏛 探索景点</h1>
        <p>发现全国热门旅游目的地</p>
      </div>

      <div class="filter-bar">
        <div class="filter-search">
          <el-icon><Search /></el-icon>
          <input v-model="query.keyword" placeholder="搜索景点..." @keyup.enter="load" />
        </div>
        <el-select v-model="query.city" placeholder="全部城市" clearable @change="load" class="fsel">
          <el-option v-for="c in cities" :key="c" :label="c" :value="c" />
        </el-select>
        <el-select v-model="query.category" placeholder="全部分类" clearable @change="load" class="fsel">
          <el-option v-for="c in cats" :key="c" :label="c" :value="c" />
        </el-select>
        <el-select v-model="query.sortBy" @change="load" class="fsort">
          <el-option label="⭐ 评分最高" value="rating" />
          <el-option label="💬 评价最多" value="review_count" />
          <el-option label="🆕 最新发布" value="create_time" />
        </el-select>
      </div>

      <div class="card-grid cols-4" v-if="list.length>0">
        <div v-for="item in list" :key="item.id" class="spot-card" @click="$router.push(`/attractions/${item.id}`)">
          <div class="card-img">
            <img :src="item.imageUrl || fallback" :alt="item.name" @error="e=>e.target.src=fallback" />
            <div class="card-overlay">
              <span class="card-city">{{ item.city }}</span>
              <span class="card-cat">{{ item.category }}</span>
            </div>
            <div class="price-tag" :class="{free:item.ticketPrice===0}">
              {{ item.ticketPrice > 0 ? '¥'+item.ticketPrice : '免费' }}
            </div>
          </div>
          <div class="card-body">
            <h3>{{ item.name }}</h3>
            <p class="card-desc">{{ item.description?.substring(0, 60) }}...</p>
            <div class="card-meta">
              <span class="stars">⭐ {{ item.rating }}</span>
              <span class="review-count">{{ item.reviewCount }} 条评价</span>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无数据" />

      <div class="pager" v-if="total>0">
        <el-pagination v-model:current-page="query.page" :total="total" :page-size="query.size" background />
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getAttractions, getCities } from '@/api/attraction'

const route = useRoute()
const list=ref([]), cities=ref([]), total=ref(0)
const cats=['自然风光','历史古迹','主题公园','博物馆','古镇','海滩','山脉','其他']
const fallback='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjI4MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9ImciIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPjxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM2NjdlYWEiLz48c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiM3NjRiYTIiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2cpIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjIwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iIG9wYWNpdHk9IjAuOCI+5Zu+54mH5Yqg6L295LitPC90ZXh0Pjwvc3ZnPg=='
const query = reactive({page:1,size:12,keyword:route.query.keyword||'',city:'',category:'',sortBy:'rating'})

onMounted(async ()=>{ await loadCities(); load() })
watch(()=>query.page, load)

async function loadCities(){try{const r=await getCities();cities.value=r.data||[]}catch{}}
async function load(){try{const r=await getAttractions({...query});list.value=r.data.records;total.value=r.data.total}catch{}}
</script>

<style scoped>
.list-page { min-height: 100vh; background: #f0f2f5; }
.container { max-width: 1400px; margin: 0 auto; padding: 32px 24px; }
.page-header { text-align: center; margin-bottom: 32px; }
.page-header h1 { font-size: 32px; font-weight: 800; margin-bottom: 8px; }
.page-header p { color: #999; font-size: 16px; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 28px; justify-content: center; flex-wrap: wrap; }
.filter-search {
  display: flex; align-items: center; gap: 8px; padding: 10px 18px;
  background: #fff; border-radius: 30px; border: 2px solid #eee; width: 280px; transition: all 0.3s;
}
.filter-search:focus-within { border-color: #667eea; box-shadow: 0 0 0 4px rgba(102,126,234,0.1); }
.filter-search input { border: none; outline: none; font-size: 14px; width: 100%; }
.filter-search .el-icon { color: #999; }
.fsel { width: 150px; } .fsort { width: 170px; }

.card-grid { display: grid; gap: 20px; }
.cols-4 { grid-template-columns: repeat(4, 1fr); }
@media (max-width: 1200px) { .cols-4 { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .cols-4 { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 500px) { .cols-4 { grid-template-columns: 1fr; } }

.spot-card {
  background: #fff; border-radius: 16px; overflow: hidden; cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04); transition: all 0.3s;
}
.spot-card:hover { transform: translateY(-6px); box-shadow: 0 12px 30px rgba(0,0,0,0.1); }
.card-img { position: relative; height: 200px; overflow: hidden; }
.card-img img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s; }
.spot-card:hover .card-img img { transform: scale(1.08); }
.card-overlay {
  position: absolute; top: 12px; left: 12px; right: 12px; display: flex; justify-content: space-between;
}
.card-city { background: rgba(102,126,234,0.9); color: #fff; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.card-cat { background: rgba(255,255,255,0.9); color: #667eea; padding: 4px 12px; border-radius: 20px; font-size: 12px; }
.price-tag {
  position: absolute; bottom: 12px; right: 12px;
  background: rgba(230,162,60,0.95); color: #fff; padding: 4px 14px; border-radius: 20px;
  font-size: 16px; font-weight: 700;
}
.price-tag.free { background: rgba(103,194,58,0.95); font-size: 13px; }
.card-body { padding: 16px; }
.card-body h3 { font-size: 16px; font-weight: 600; margin-bottom: 6px; }
.card-desc { color: #999; font-size: 13px; margin-bottom: 10px; line-height: 1.5; }
.card-meta { display: flex; align-items: center; gap: 12px; }
.stars { color: #f59e0b; font-weight: 600; }
.review-count { color: #bbb; font-size: 13px; }
.pager { display: flex; justify-content: center; margin-top: 40px; }
</style>
