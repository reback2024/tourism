<template>
  <div class="list-page">
    <MainHeader />
    <div class="page-content">
      <div class="filter-bar">
        <el-input v-model="query.keyword" placeholder="搜索餐厅..." :prefix-icon="Search" clearable class="filter-search" @keyup.enter="loadData" />
        <el-select v-model="query.city" placeholder="城市" clearable @change="loadData"><el-option v-for="c in cities" :key="c" :label="c" :value="c" /></el-select>
        <el-select v-model="query.cuisineType" placeholder="菜系" clearable @change="loadData">
          <el-option v-for="t in cuisineTypes" :key="t" :label="t" :value="t" />
        </el-select>
        <el-select v-model="query.sortBy" @change="loadData">
          <el-option label="评分最高" value="rating" /><el-option label="价格最低" value="price_per_person" />
        </el-select>
      </div>
      <div class="card-grid">
        <el-card v-for="item in list" :key="item.id" shadow="hover" class="item-card" @click="$router.push(`/restaurants/${item.id}`)">
          <img :src="item.imageUrl || defaultImg" :alt="item.name" @error="e => e.target.src = defaultImg" />
          <div class="card-info">
            <h3>{{ item.name }}</h3>
            <p class="tags"><el-tag size="small">{{ item.cuisineType }}</el-tag> · {{ item.city }}</p>
            <p class="desc">{{ item.description?.substring(0, 50) }}...</p>
            <div class="card-bottom">
              <span class="rating">📊 {{ item.rating }}分</span>
              <span class="price">人均 ¥{{ item.pricePerPerson }}</span>
            </div>
          </div>
        </el-card>
      </div>
      <el-empty v-if="list.length === 0" description="暂无餐厅" />
      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination v-model:current-page="query.page" :total="total" :page-size="query.size" layout="total, prev, pager, next" @change="loadData" />
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getRestaurants } from '@/api/restaurant'
import { Search } from '@element-plus/icons-vue'

const list = ref([]), cities = ref([]), total = ref(0)
const defaultImg = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZTBlNmYwIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE4IiBmaWxsPSIjOTk5IiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBkeT0iLjNlbSI+5YWo5bqU5Zu+54mHPC90ZXh0Pjwvc3ZnPg=='
const cuisineTypes = ['本地特色','川菜','粤菜','西餐','日料','火锅','烧烤','小吃','海鲜','素食']

const query = reactive({ page: 1, size: 10, keyword: '', city: '', cuisineType: '', sortBy: 'rating' })

onMounted(loadData)

async function loadData() {
  try {
    const res = await getRestaurants({ ...query, cuisineType: query.cuisineType || undefined })
    list.value = res.data.records; total.value = res.data.total
  } catch { list.value = [] }
}
</script>

<style scoped>
.list-page { min-height: 100vh; background: #f5f7fa; }
.page-content { max-width: 1400px; margin: 24px auto; padding: 0 20px; }
.filter-bar { display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap; }
.filter-search { width: 280px; }
.card-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
@media (max-width: 1200px) { .card-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .card-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 500px) { .card-grid { grid-template-columns: 1fr; } }
.item-card { cursor: pointer; transition: transform 0.2s; }
.item-card:hover { transform: translateY(-4px); }
.item-card img { width: 100%; height: 180px; object-fit: cover; border-radius: 8px 8px 0 0; }
.card-info { padding: 14px; }
.card-info h3 { font-size: 15px; margin-bottom: 4px; }
.tags { font-size: 13px; color: #666; margin-bottom: 4px; }
.desc { color: #999; font-size: 13px; margin: 6px 0; }
.card-bottom { display: flex; justify-content: space-between; align-items: center; }
.rating { color: #ff9900; font-size: 13px; }
.price { color: #e6a23c; font-weight: bold; font-size: 14px; }
.pagination-wrap { display: flex; justify-content: center; margin-top: 40px; }
</style>
