-- ========================================
-- 智能旅游推荐平台 - 数据库建表脚本
-- 数据库: tourism_db
-- ========================================

CREATE DATABASE IF NOT EXISTS tourism_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tourism_db;

-- ==================== 1. 用户表 ====================
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    avatar VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色: USER/ADMIN',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==================== 2. 景点表 ====================
CREATE TABLE IF NOT EXISTS `attraction` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '景点ID',
    name VARCHAR(100) NOT NULL COMMENT '景点名称',
    city VARCHAR(50) NOT NULL COMMENT '所在城市',
    category VARCHAR(30) NOT NULL COMMENT '分类: 自然风光/历史古迹/主题公园/博物馆/古镇/海滩/山脉/其他',
    description TEXT DEFAULT NULL COMMENT '景点介绍',
    address VARCHAR(300) DEFAULT NULL COMMENT '详细地址',
    ticket_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '门票价格',
    opening_hours VARCHAR(100) DEFAULT NULL COMMENT '开放时间',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    images TEXT DEFAULT NULL COMMENT '多图(JSON数组)',
    rating DECIMAL(2,1) DEFAULT 0.0 COMMENT '综合评分(0-5.0)',
    review_count INT DEFAULT 0 COMMENT '评价数量',
    latitude DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    longitude DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-上架, 0-下架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_city (city),
    INDEX idx_category (category),
    INDEX idx_rating (rating),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- ==================== 3. 酒店表 ====================
CREATE TABLE IF NOT EXISTS `hotel` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '酒店ID',
    name VARCHAR(100) NOT NULL COMMENT '酒店名称',
    city VARCHAR(50) NOT NULL COMMENT '所在城市',
    address VARCHAR(300) DEFAULT NULL COMMENT '详细地址',
    star_level TINYINT NOT NULL DEFAULT 3 COMMENT '星级(1-5)',
    price_range VARCHAR(20) DEFAULT NULL COMMENT '价格区间描述',
    price_min DECIMAL(10,2) DEFAULT NULL COMMENT '最低价格',
    price_max DECIMAL(10,2) DEFAULT NULL COMMENT '最高价格',
    description TEXT DEFAULT NULL COMMENT '酒店描述',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    images TEXT DEFAULT NULL COMMENT '多图(JSON数组)',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    rating DECIMAL(2,1) DEFAULT 0.0 COMMENT '综合评分',
    review_count INT DEFAULT 0 COMMENT '评价数量',
    latitude DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    longitude DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-上架, 0-下架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_city (city),
    INDEX idx_star_level (star_level),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酒店表';

-- ==================== 4. 餐厅表 ====================
CREATE TABLE IF NOT EXISTS `restaurant` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '餐厅ID',
    name VARCHAR(100) NOT NULL COMMENT '餐厅名称',
    city VARCHAR(50) NOT NULL COMMENT '所在城市',
    address VARCHAR(300) DEFAULT NULL COMMENT '详细地址',
    cuisine_type VARCHAR(30) NOT NULL COMMENT '菜系',
    price_range VARCHAR(20) DEFAULT NULL COMMENT '人均消费描述',
    price_per_person DECIMAL(10,2) DEFAULT NULL COMMENT '人均价格',
    description TEXT DEFAULT NULL COMMENT '餐厅描述',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    images TEXT DEFAULT NULL COMMENT '多图(JSON数组)',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    rating DECIMAL(2,1) DEFAULT 0.0 COMMENT '综合评分',
    review_count INT DEFAULT 0 COMMENT '评价数量',
    latitude DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    longitude DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-上架, 0-下架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_city (city),
    INDEX idx_cuisine_type (cuisine_type),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐厅表';

-- ==================== 5. 评价表 ====================
CREATE TABLE IF NOT EXISTS `review` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    user_id BIGINT NOT NULL COMMENT '评价用户ID',
    target_type VARCHAR(20) NOT NULL COMMENT '目标类型: ATTRACTION/HOTEL/RESTAURANT',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    rating TINYINT NOT NULL COMMENT '评分(1-5)',
    content VARCHAR(1000) NOT NULL COMMENT '评价内容',
    images TEXT DEFAULT NULL COMMENT '图片(JSON数组)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 0-删除, -1-审核不通过',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_target (user_id, target_type, target_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ==================== 6. 收藏表 ====================
CREATE TABLE IF NOT EXISTS `favorite` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    target_type VARCHAR(20) NOT NULL COMMENT '收藏类型: ATTRACTION/HOTEL/RESTAURANT',
    target_id BIGINT NOT NULL COMMENT '收藏目标ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_target (user_id, target_type, target_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ==================== 7. 游记表 ====================
CREATE TABLE IF NOT EXISTS `travel_note` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '游记ID',
    user_id BIGINT NOT NULL COMMENT '作者用户ID',
    title VARCHAR(200) NOT NULL COMMENT '游记标题',
    content LONGTEXT NOT NULL COMMENT '游记内容(富文本HTML)',
    cover_image VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    destination VARCHAR(50) DEFAULT NULL COMMENT '目的地城市',
    tags VARCHAR(300) DEFAULT NULL COMMENT '标签(逗号分隔)',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-发布, 0-草稿, -1-审核不通过',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_destination (destination),
    INDEX idx_create_time (create_time),
    INDEX idx_view_count (view_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游记表';

-- ==================== 8. 行程表 ====================
CREATE TABLE IF NOT EXISTS `itinerary` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '行程ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '行程标题',
    destination VARCHAR(50) NOT NULL COMMENT '目的地城市',
    days TINYINT NOT NULL COMMENT '行程天数',
    budget DECIMAL(10,2) DEFAULT NULL COMMENT '预算金额',
    preferences VARCHAR(300) DEFAULT NULL COMMENT '偏好标签(逗号分隔)',
    content LONGTEXT DEFAULT NULL COMMENT '行程完整内容(AI生成)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 0-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_destination (destination),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行程表';

-- ==================== 9. 行程日明细表 ====================
CREATE TABLE IF NOT EXISTS `itinerary_day` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    itinerary_id BIGINT NOT NULL COMMENT '所属行程ID',
    day_number TINYINT NOT NULL COMMENT '第几天',
    attractions TEXT DEFAULT NULL COMMENT '当日景点(JSON)',
    hotels TEXT DEFAULT NULL COMMENT '当日住宿(JSON)',
    restaurants TEXT DEFAULT NULL COMMENT '当日推荐餐厅(JSON)',
    notes VARCHAR(500) DEFAULT NULL COMMENT '当日备注',
    UNIQUE KEY uk_itinerary_day (itinerary_id, day_number),
    INDEX idx_itinerary_id (itinerary_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行程日明细表';

-- ==================== 10. 点赞表 ====================
CREATE TABLE IF NOT EXISTS `like` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    target_type VARCHAR(20) NOT NULL COMMENT '点赞类型: TRAVEL_NOTE/REVIEW',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_target (user_id, target_type, target_id),
    INDEX idx_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- ==================== 11. 系统配置表 ====================
CREATE TABLE IF NOT EXISTS `sys_config` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT DEFAULT NULL COMMENT '配置值',
    description VARCHAR(500) DEFAULT NULL COMMENT '配置说明',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ==================== 12. 操作日志表 ====================
CREATE TABLE IF NOT EXISTS `system_log` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    admin_id BIGINT NOT NULL COMMENT '操作管理员ID',
    action VARCHAR(50) NOT NULL COMMENT '操作类型: CREATE/UPDATE/DELETE/AUDIT',
    target VARCHAR(100) NOT NULL COMMENT '操作对象描述',
    detail TEXT DEFAULT NULL COMMENT '操作详情(JSON)',
    ip VARCHAR(45) DEFAULT NULL COMMENT '操作IP',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_admin_id (admin_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
