# 兴启电商企业 ERP 管理系统 (Mall ERP)

一套面向中小型电商企业的综合 ERP 管理系统，涵盖用户权限、商品管理、出入库管理、订单流转、财务统计与多维度报表分析。基于前后端分离架构设计与实现。

---


https://github.com/user-attachments/assets/7367fd19-c97f-4e4b-9def-c78dc8279a7f


## 🛠️ 技术栈

### 后端技术栈 (Server)
- **核心框架**：Spring Boot 3.5.x
- **持久层框架**：MyBatis-Plus 3.5.x
- **安全认证**：Spring Security + JWT / Token
- **数据库**：MySQL 8.0+
- **参数校验**：Hibernate Validator
- **辅助工具**：Lombok、Maven

### 前端技术栈 (Client)
- **核心框架**：Vue 3 (Composition API / `<script setup>`)
- **构建工具**：Vite
- **UI 组件库**：Element Plus
- **路由管理**：Vue Router 4
- **状态管理**：Pinia
- **网络请求**：Axios

---

## 👥 角色权限体系

系统细分三个典型业务角色：

| 角色 | 权限说明 |
| :--- | :--- |
| **超级管理员 (Super Admin)** | 系统最高权限。管理普通管理员（增删改查）、用户管理、查看财务报表总账、系统级全局配置等。 |
| **普通管理员 (Admin)** | 日常运营管理。负责商品维护、库存出入库管理、订单发货处理、支付状态确认与日常统计。 |
| **普通用户 (User)** | 客户端用户。前台浏览商品详情、在线下单、查看个人订单流转、个人中心资料维护（头像、昵称修改等）。 |

---

## 📁 目录结构

```text
mall/
├── server/                                  # Spring Boot 后端工程
│   ├── src/main/java/                       # 核心 Java 源码
│   ├── src/main/resources/                  # 配置文件 (application.yml 等)
│   └── pom.xml                              # Maven 依赖配置
├── vue3-project/                            # Vue3 前端工程
│   ├── src/                                 # 页面与组件源码
│   ├── index.html                           # 入口页面
│   ├── vite.config.js                       # Vite 构建配置
│   └── package.json                         # 前端依赖配置
├── _localhost-2026_03_08_13_25_18-dump.sql  # 数据库结构及初始化数据文件
└── README.md                                # 项目使用说明文档
```

---

## 🚀 快速启动指南

### 1. 数据库准备
1. 安装并启动 **MySQL 8.0+** 服务。
2. 创建数据库（如 `mall` 或自定义库名，字符集推荐 `utf8mb4`）。
3. 导入根目录下的数据库脚本文件：
   ```bash
   mysql -u root -p mall < _localhost-2026_03_08_13_25_18-dump.sql
   ```

### 2. 后端服务启动 (`server`)
1. 使用 IntelliJ IDEA 或 Eclipse 打开 `server` 目录。
2. 检查 `src/main/resources/application.yml`（或 `application.properties`），修改其中的 MySQL 连接账号与密码。
3. 等待 Maven 依赖加载完成。
4. 运行主启动类 `ServerApplication.java` 即可启动后端接口服务。

### 3. 前端服务启动 (`vue3-project`)
1. 进入前端工程目录：
   ```bash
   cd vue3-project
   ```
2. 安装项目依赖：
   ```bash
   npm install
   ```
3. 启动本地开发服务器：
   ```bash
   npm run dev
   ```
4. 启动成功后，浏览器访问控制台提示的本地地址（通常为 `http://localhost:5173`）。

---

## 📄 许可证
本项目采用 MIT 许可证，仅用于学习与交流。
