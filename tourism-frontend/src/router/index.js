import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const routes = [
  // ========== 前台路由 ==========
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: { title: '首页', public: true }
  },
  {
    path: '/attractions',
    name: 'AttractionList',
    component: () => import('@/views/attraction/AttractionList.vue'),
    meta: { title: '景点推荐', public: true }
  },
  {
    path: '/attractions/:id',
    name: 'AttractionDetail',
    component: () => import('@/views/attraction/AttractionDetail.vue'),
    meta: { title: '景点详情', public: true }
  },
  {
    path: '/hotels',
    name: 'HotelList',
    component: () => import('@/views/hotel/HotelList.vue'),
    meta: { title: '酒店推荐', public: true }
  },
  {
    path: '/hotels/:id',
    name: 'HotelDetail',
    component: () => import('@/views/hotel/HotelDetail.vue'),
    meta: { title: '酒店详情', public: true }
  },
  {
    path: '/restaurants',
    name: 'RestaurantList',
    component: () => import('@/views/restaurant/RestaurantList.vue'),
    meta: { title: '美食推荐', public: true }
  },
  {
    path: '/restaurants/:id',
    name: 'RestaurantDetail',
    component: () => import('@/views/restaurant/RestaurantDetail.vue'),
    meta: { title: '餐厅详情', public: true }
  },
  {
    path: '/travel-notes',
    name: 'TravelNoteList',
    component: () => import('@/views/note/TravelNoteList.vue'),
    meta: { title: '游记分享', public: true }
  },
  {
    path: '/travel-notes/:id',
    name: 'TravelNoteDetail',
    component: () => import('@/views/note/TravelNoteDetail.vue'),
    meta: { title: '游记详情', public: true }
  },
  {
    path: '/travel-notes/write',
    name: 'TravelNoteEditor',
    component: () => import('@/views/note/TravelNoteEditor.vue'),
    meta: { title: '写游记', requiresAuth: true }
  },
  {
    path: '/travel-notes/:id/edit',
    name: 'TravelNoteEdit',
    component: () => import('@/views/note/TravelNoteEditor.vue'),
    meta: { title: '编辑游记', requiresAuth: true }
  },
  {
    path: '/itinerary/plan',
    name: 'ItineraryPlan',
    component: () => import('@/views/itinerary/ItineraryPlan.vue'),
    meta: { title: 'AI行程规划', requiresAuth: true }
  },
  {
    path: '/itinerary/list',
    name: 'ItineraryList',
    component: () => import('@/views/itinerary/ItineraryList.vue'),
    meta: { title: '我的行程', requiresAuth: true }
  },
  {
    path: '/itinerary/:id',
    name: 'ItineraryDetail',
    component: () => import('@/views/itinerary/ItineraryDetail.vue'),
    meta: { title: '行程详情', requiresAuth: true }
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    component: () => import('@/views/user/ProfileView.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  {
    path: '/user/favorites',
    name: 'UserFavorites',
    component: () => import('@/views/user/FavoritesView.vue'),
    meta: { title: '我的收藏', requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/LoginView.vue'),
    meta: { title: '登录', public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/user/RegisterView.vue'),
    meta: { title: '注册', public: true }
  },

  // ========== 管理后台 ==========
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/DashboardView.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'attractions',
        name: 'AdminAttractions',
        component: () => import('@/views/admin/AttractionManage.vue'),
        meta: { title: '景点管理' }
      },
      {
        path: 'hotels',
        name: 'AdminHotels',
        component: () => import('@/views/admin/HotelManage.vue'),
        meta: { title: '酒店管理' }
      },
      {
        path: 'restaurants',
        name: 'AdminRestaurants',
        component: () => import('@/views/admin/RestaurantManage.vue'),
        meta: { title: '餐厅管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'reviews',
        name: 'AdminReviewAudit',
        component: () => import('@/views/admin/ReviewAudit.vue'),
        meta: { title: '内容审核' }
      },
      {
        path: 'config',
        name: 'AdminConfig',
        component: () => import('@/views/admin/SysConfig.vue'),
        meta: { title: '系统配置' }
      }
    ]
  },

  // ========== 404 ==========
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '404', public: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

// ========== 路由守卫 ==========
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 智能旅游推荐平台` : '智能旅游推荐平台'

  // 公开页面直接放行
  if (to.meta.public) {
    next()
    return
  }

  const userStore = useUserStore()

  // 需要登录
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }

  // 需要管理员
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next({ path: '/' })
    return
  }

  next()
})

export default router
