<template>
  <div class="list-page">
    <MainHeader />
    <div class="container">
      <div class="page-header"><h1>🍜 美食推荐</h1><p>品尝地道美味</p></div>
      <div class="filter-bar">
        <div class="filter-search"><el-icon><Search /></el-icon><input v-model="query.keyword" placeholder="搜索餐厅..." @keyup.enter="load" /></div>
        <el-select v-model="query.city" placeholder="城市" clearable @change="load"><el-option v-for="c in cities" :key="c" :label="c" :value="c" /></el-select>
        <el-select v-model="query.cuisineType" placeholder="菜系" clearable @change="load"><el-option v-for="t in types" :key="t" :label="t" :value="t" /></el-select>
        <el-select v-model="query.sortBy" @change="load"><el-option label="⭐ 评分最高" value="rating"/><el-option label="💰 人均最低" value="price_per_person"/></el-select>
      </div>
      <div class="card-grid cols-3" v-if="list.length>0">
        <div v-for="item in list" :key="item.id" class="spot-card" @click="$router.push(`/restaurants/${item.id}`)">
          <div class="card-img"><img :src="item.imageUrl||fallback" :alt="item.name" @error="e=>e.target.src=fallback" /><div class="price-tag">¥{{item.pricePerPerson}}/人</div></div>
          <div class="card-body">
            <h3>{{ item.name }}</h3>
            <p class="tags"><el-tag size="small" effect="plain">{{ item.cuisineType }}</el-tag> · {{ item.city }}</p>
            <p class="desc">{{ item.description?.substring(0,50) }}...</p>
            <div class="card-meta"><span class="rating">📊 {{ item.rating }}分</span><span class="revs">{{ item.reviewCount }}评</span></div>
          </div>
        </div>
      </div>
      <el-empty v-else /><div class="pager"><el-pagination v-model:current-page="query.page" :total="total" :page-size="query.size" background @change="load"/></div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import {ref,reactive,onMounted,watch} from 'vue'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import {getRestaurants} from '@/api/restaurant'
const list=ref([]),cities=ref([]),total=ref(0)
const types=['本地特色','川菜','粤菜','西餐','日料','火锅','烧烤','小吃','海鲜','素食']
const fallback='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjI0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9ImciIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPjxzdG9wIG9mZnNldD0iMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiNlNmEyM2MiLz48c3RvcCBvZmZzZXQ9IjEwMCUiIHN0eWxlPSJzdG9wLWNvbG9yOiNmNTZjNmMiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2cpIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjIwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iIG9wYWNpdHk9IjAuOCI+5YWo5bqU5Zu+54mHPC90ZXh0Pjwvc3ZnPg=='
const query=reactive({page:1,size:9,keyword:'',city:'',cuisineType:'',sortBy:'rating'})
onMounted(load);watch(()=>query.page,load)
async function load(){try{const r=await getRestaurants({...query,cuisineType:query.cuisineType||undefined});list.value=r.data.records;total.value=r.data.total}catch{}}
</script>

<style scoped>
.list-page{min-height:100vh;background:#f0f2f5}
.container{max-width:1300px;margin:0 auto;padding:32px 24px}
.page-header{text-align:center;margin-bottom:28px}
.page-header h1{font-size:32px;font-weight:800;margin-bottom:8px}
.page-header p{color:#999;font-size:16px}
.filter-bar{display:flex;gap:12px;margin-bottom:28px;justify-content:center;flex-wrap:wrap}
.filter-search{display:flex;align-items:center;gap:8px;padding:10px 18px;background:#fff;border-radius:30px;border:2px solid #eee;width:260px}
.filter-search:focus-within{border-color:#e6a23c;box-shadow:0 0 0 4px rgba(230,162,60,.1)}
.filter-search input{border:none;outline:none;font-size:14px;width:100%}
.card-grid{display:grid;gap:20px}
.cols-3{grid-template-columns:repeat(3,1fr)}
@media(max-width:900px){.cols-3{grid-template-columns:repeat(2,1fr)}}
@media(max-width:500px){.cols-3{grid-template-columns:1fr}}
.spot-card{background:#fff;border-radius:16px;overflow:hidden;cursor:pointer;box-shadow:0 2px 8px rgba(0,0,0,.04);transition:all .3s}
.spot-card:hover{transform:translateY(-6px);box-shadow:0 12px 30px rgba(0,0,0,.1)}
.card-img{position:relative;height:200px;overflow:hidden}
.card-img img{width:100%;height:100%;object-fit:cover;transition:transform .4s}
.spot-card:hover .card-img img{transform:scale(1.08)}
.price-tag{position:absolute;bottom:12px;right:12px;background:rgba(230,162,60,.95);color:#fff;padding:4px 14px;border-radius:20px;font-size:14px;font-weight:700}
.card-body{padding:16px}
.card-body h3{font-size:16px;font-weight:600;margin-bottom:6px}
.tags{font-size:13px;color:#666;margin-bottom:4px}
.desc{color:#999;font-size:13px;margin:6px 0;line-height:1.5}
.card-meta{display:flex;align-items:center;gap:12px}
.rating{color:#f59e0b;font-weight:600}
.revs{color:#bbb;font-size:13px}
.pager{display:flex;justify-content:center;margin-top:40px}
</style>
