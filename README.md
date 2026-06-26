# project

#### 介绍
没有

#### 软件架构
没有

#### 安装教程

  1. 安装前端依赖
  cd tourism-frontend
  npm install

  2. Navicat 连接 MySQL，执行这俩文件：
  tourism-backend/src/main/resources/db/schema.sql
  tourism-backend/src/main/resources/db/data.sql

  3. 配置智谱 API Key（在 Navicat 中执行）：
  INSERT INTO tourism_db.sys_config (config_key, config_value) VALUES ('zhipu_api_key', '你的Key');
  这里可以用https://bigmodel.cn/
  智谱ai，里面有免费的api“GLM-4.7-Flash”可以调用

  4. 双击 start.bat 启动

#### 使用说明
没有

#### 参与贡献
没有


#### 特技
没有
