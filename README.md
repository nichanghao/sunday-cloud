<p align="center">
 <img src="https://img.shields.io/badge/Gradle-8.8-green" alt="Downloads">
 <img src="https://img.shields.io/badge/Java-21-green" alt="Downloads">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2023.0.3-blue" alt="Downloads">
 <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibana-2023.0.1-blue" alt="Downloads">
 <img src="https://img.shields.io/badge/Spring%20Boot-3.3.3-blue" alt="Downloads">
 <img src="https://img.shields.io/badge/Dubbo-3.2.15-blue" alt="Downloads">
</p>


# 项目介绍

Sunday Cloud 是一个基于 Spring Cloud 和 Dubbo 开发的权限管理平台，集成了jwt鉴权，动态路由，菜单权限，按钮权限，RBAC等功能。

# 项目截图
1. 登录页面
![登录](/_images/login.jpg)
2. 首页
![首页](/_images/home.jpg)
3. 菜单管理
![菜单管理](/_images/menu-manage.jpg)
4. 分配菜单
![分配菜单](/_images/assign-menus.jpg)

# 快速开始

### 环境准备
```angular2html
- java 21+
- nacos 2.2.3+
- mysql 8.0+
- redis 7.0+
- node.js 18.19.0+
```
### 数据初始化
1. 下载项目
```shell
git clone git@gitee.com:nichanghao/sunday-cloud.git
```
2. 使用docker compose启动mysql、redis、nacos
```shell
cd docker/
docker-compose up -d
```
3. 创建数据库
```shell
CREATE DATABASE sunday;
```
4. 导入数据文件：sql/mysql/sunday.sql

### 运行项目
1. 后端
```shell
# 编译后端服务（win系统使用gradlew.bat，linux/mac系统使用gradlew）
./gradlew clean build --console=plain

# 启动 sunday-system-application
cd sunday-system/sunday-system-application/build/libs
java -jar sunday-system-application-1.0-SNAPSHOT.jar

# 启动 sunday-gateway
cd sunday-gateway/build/libs
java -jar sunday-gateway-1.0-SNAPSHOT.jar
```
2. 前端
```shell
cd ui
pnpm i
pnpm dev
```
3. 打开浏览器访问 http://localhost:9527  
username：`sunday`  
password：`123456`


