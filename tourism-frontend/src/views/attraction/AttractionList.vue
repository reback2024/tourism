<template>
  <div class="list-page">
    <MainHeader />

    <div class="page-content">
      <!-- 搜索和筛选区域 -->
      <div class="filter-bar">
        <el-input
          v-model="query.keyword"
          placeholder="搜索景点名称..."
          :prefix-icon="Search"
          clearable
          class="filter-search"
          @keyup.enter="handleSearch"
        />

        <el-select v-model="query.city" placeholder="选择城市" clearable class="filter-select" @change="handleSearch">
          <el-option v-for="city in cities" :key="city" :label="city" :value="city" />
        </el-select>

        <el-select v-model="query.category" placeholder="选择分类" clearable class="filter-select" @change="handleSearch">
          <el-option label="自然风光" value="自然风光" />
          <el-option label="历史古迹" value="历史古迹" />
          <el-option label="主题公园" value="主题公园" />
          <el-option label="博物馆" value="博物馆" />
          <el-option label="古镇" value="古镇" />
          <el-option label="海滩" value="海滩" />
          <el-option label="山脉" value="山脉" />
          <el-option label="其他" value="其他" />
        </el-select>

        <el-select v-model="query.sortBy" class="filter-sort" @change="handleSearch">
          <el-option label="综合评分" value="rating" />
          <el-option label="评价最多" value="review_count" />
          <el-option label="最新发布" value="create_time" />
        </el-select>
      </div>

      <!-- 景点列表 -->
      <div class="attraction-grid">
        <el-card
          v-for="item in list"
          :key="item.id"
          :body-style="{ padding: '0' }"
          shadow="hover"
          class="attraction-card"
          @click="$router.push(`/attractions/${item.id}`)"
        >
          <div class="card-image">
            <img :src="item.imageUrl || defaultImg" :alt="item.name" @error="e => e.target.src = defaultImg" />
            <div class="card-tag">{{ item.city }} · {{ item.category }}</div>
          </div>
          <div class="card-info">
            <h3>{{ item.name }}</h3>
            <p class="card-desc">{{ item.description?.substring(0, 60) }}...</p>
            <div class="card-bottom">
              <el-rate :model-value="Number(item.rating)" disabled text-color="#ff9900" size="small" />
              <span class="rating-text">{{ item.rating }}分 ({{ item.reviewCount }}评价)</span>
            </div>
            <div class="card-price" v-if="item.ticketPrice > 0">¥{{ item.ticketPrice }}</div>
            <div class="card-price free" v-else>免费</div>
          </div>
        </el-card>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!loading && list.length === 0" description="暂无景点数据" />

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          :page-sizes="[10, 20, 30]"
          layout="total, sizes, prev, pager, next"
          @change="handleSearch"
        />
      </div>
    </div>

    <MainFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/layout/MainHeader.vue'
import MainFooter from '@/components/layout/MainFooter.vue'
import { getAttractions, getCities } from '@/api/attraction'
import { Search } from '@element-plus/icons-vue'

const route = useRoute()
const list = ref([])
const cities = ref([])
const total = ref(0)
const loading = ref(false)
const defaultImg = 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400'

const query = reactive({
  page: 1,
  size: 10,
  keyword: route.query.keyword || '',
  city: '',
  category: '',
  sortBy: 'rating'
})

onMounted(() => {
  loadCities()
  handleSearch()
})

async function loadCities() {
  try {
    const res = await getCities()
    cities.value = res.data || []
  } catch (e) { /* ignore */ }
}

async function handleSearch() {
  loading.value = true
  try {
    const res = await getAttractions({ ...query })
    const data = res.data
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.list-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.page-content {
  flex: 1;
  max-width: 1400px;
  margin: 24px auto;
  padding: 0 20px;
  width: 100%;
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-search {
  width: 280px;
}

.filter-select {
  width: 160px;
}

.filter-sort {
  width: 140px;
}

.attraction-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

@media (max-width: 1200px) { .attraction-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .attraction-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 500px) { .attraction-grid { grid-template-columns: 1fr; } }

.attraction-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.attraction-card:hover { transform: translateY(-4px); }

.card-image {
  height: 180px;
  overflow: hidden;
  position: relative;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(64, 158, 255, 0.85);
  color: #fff;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.card-info {
  padding: 16px;
  position: relative;
}

.card-info h3 { font-size: 16px; margin-bottom: 6px; }

.card-desc {
  color: #999;
  font-size: 13px;
  margin-bottom: 10px;
  line-height: 1.5;
}

.card-bottom {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.rating-text { font-size: 13px; color: #ff9900; }

.card-price {
  position: absolute;
  top: 16px;
  right: 16px;
  font-size: 20px;
  font-weight: bold;
  color: #e6a23c;
}

.card-price.free {
  color: #67c23a;
  font-size: 14px;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
