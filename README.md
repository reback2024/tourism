# 🏔 智能旅游推荐平台

> SpringBoot + Vue3 + MyBatis Plus + 智谱AI 全栈项目

[![GitHub](https://img.shields.io/badge/GitHub-reback2024%2Ftourism-blue)](https://github.com/reback2024/tourism)
[![Gitee](https://img.shields.io/badge/Gitee-re__back%2Fproject-red)](https://gitee.com/re_back/project)

## ✨ 功能

| 模块 | 功能 |
|------|------|
| 🔐 认证 | JWT登录注册，游客/用户/管理员三级权限 |
| 🏛 景点 | 浏览搜索、分类筛选、评分排序、热门推荐 |
| 🏨 酒店 | 星级筛选、价格排序、详情评价 |
| 🍜 美食 | 菜系筛选、人均排序、详情评价 |
| ⭐ 评价 | 发表评价、评分自动计算、我的评价 |
| ❤️ 收藏 | 添加/取消收藏、批量检查、我的收藏 |
| 📝 游记 | 富文本编辑、浏览点赞、热门推荐 |
| 🤖 AI | 智谱GLM-4 行程规划 + AI旅游助手对话 |
| 🛠 后台 | 数据概览、CRUD管理、内容审核、系统配置 |

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 3.4 + MyBatis Plus + MySQL 8.0 + JWT |
| 前端 | Vue 3 + Element Plus + Vite + Pinia + Axios |
| AI | 智谱AI (GLM-4-Flash) |
| 数据库 | MySQL 8.0（12张表，70+接口） |
| 部署 | Docker + Docker Compose |

## 🚀 快速启动

### 1. 准备环境
- JDK 24+
- MySQL 8.0
- Node.js 20+
- 智谱AI API Key（[免费申请](https://open.bigmodel.cn)）

### 2. 初始化数据库
用 Navicat 连接 MySQL，执行：
- `tourism-backend/src/main/resources/db/schema.sql`
- `tourism-backend/src/main/resources/db/data.sql`

### 3. 配置 API Key
```sql
INSERT INTO tourism_db.sys_config (config_key, config_value) VALUES ('zhipu_api_key', '你的Key');
```

### 4. 启动
双击项目根目录的 `start.bat`，或分别启动：

```bash
# 后端
cd tourism-backend
mvnw spring-boot:run

# 前端
cd tourism-frontend
npm install
npm run dev
```

### 5. 访问
- 前台：`http://localhost:5173`
- 后台：`http://localhost:5173/admin`
- API文档：`http://localhost:8080/swagger-ui/index.html`
- 管理员：`admin / admin123`

## 🐳 Docker 部署

```bash
docker-compose up -d
```

## 📸 截图

| 首页 | 景点列表 | AI行程 | 管理后台 |
|:---:|:---:|:---:|:---:|
| ![首页](screenshots/home.png) | ![景点](screenshots/attractions.png) | ![AI](screenshots/ai.png) | ![后台](screenshots/admin.png) |

## 📁 项目结构

```
智能旅游项目/
├── tourism-backend/       # Spring Boot 后端
│   └── src/main/java/com/tourism/
│       ├── controller/    # 控制器（前台 + admin）
│       ├── service/       # 业务层
│       ├── mapper/        # MyBatis Plus
│       ├── entity/        # 实体类
│       ├── security/      # JWT安全模块
│       └── common/        # 通用模块
├── tourism-frontend/      # Vue 3 前端
│   └── src/
│       ├── views/         # 28个页面
│       ├── components/    # 公共组件
│       ├── router/        # 路由 + 守卫
│       ├── stores/        # Pinia状态
│       └── api/           # API封装
├── docker-compose.yml     # Docker部署
└── start.bat              # 一键启动
```
