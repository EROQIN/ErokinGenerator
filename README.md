# Erokin Generator - 代码生成器共享平台

> 作者：Erokin

**Erokin Generator** 是一个功能强大的代码生成器共享平台，基于 React + Spring Boot + Picocli + 对象存储构建。它提供从本地命令行工具到在线可视化平台的全方位解决方案，旨在提升开发者效率。

开发者可以在平台上轻松制作并发布自己的代码生成器，而用户则可以方便地搜索、下载及在线使用这些生成器。平台还内置了后台管理功能，用于维护所有用户和生成器资源。

## 项目背景

本项目的核心理念是“工具的工具”。它始于一个简单的命令行代码生成器，用于解决开发中的重复性编码问题。随着功能的迭代，它演变为一个通用的代码生成器制作工具，最终进化为一个成熟的在线共享平台。

项目在结构上分为三个主要部分，体现了逐步迭代和功能完善的开发过程：

1.  **本地代码生成器**：一个可以通过命令行高效生成定制化代码的基础工具。
2.  **代码生成器制作工具**：将核心生成能力封装成一个更通用的“工具的工具”，让创建新的生成器变得简单。
3.  **在线代码生成器平台**：将本地工具搬到线上，构建一个可以让开发者分享、用户在线使用代码生成器的Web应用。

## 主要特点

- **全栈解决方案**：提供从后端到前端，从命令行到Web界面的完整体验。
- **高度可定制**：支持通过元信息轻松定义和创建高度定制化的代码生成器。
- **在线化与协作**：用户不仅可以在线使用代码生成器，还可以分享自己的作品，形成协作社区。
- **技术栈先进**：采用业界主流的前后端分离技术栈，并结合多种设计模式与优化方案，确保项目的高性能与可扩展性。

## 技术选型

#### 前端

- React 18 开发框架
- Ant Design Pro 脚手架
- Ant Design 组件库
- Ant Design Procomponents 高级组件
- OpenAPI 代码生成
- 前端工程化：ESLint + Prettier + TypeScript
- 通用文件上传下载方案

#### 后端

- Java Spring Boot 开发框架
- MySQL 数据库
- MyBatis-Plus & MyBatis X
- Maven
- Picocli Java 命令行应用开发
- FreeMarker 模板引擎
- Caffeine + Redis 多级缓存
- XXL-JOB 分布式任务调度系统
- 腾讯云 COS 对象存储
- 多种设计模式（命令模式、模板方法模式、单例模式等）
- Vert.x 响应式编程
- JMeter 压力测试

#### 部署

- 轻量应用服务器
- 宝塔 Linux 面板
- Nginx 反向代理

## 如何使用

### 环境要求

- Node.js >= 12.0.0
- JDK 1.8
- Maven
- MySQL
- Redis

### 本地运行

1.  **克隆项目**

    ```bash
    git clone <your-repo-url>
    cd Erokin-generator
    ```

2.  **后端配置**

    - 修改 `Erokin-generator-web-backend/src/main/resources/application.yml` 文件中的数据库、Redis和对象存储配置。
    - 在数据库中创建名为 `erokin_generator` 的数据库，并执行 `Erokin-generator-web-backend/sql/create_table.sql` 中的SQL脚本。

3.  **启动后端服务**

    - 首先，需要打包 `Erokin-generator-maker` 模块：
      ```bash
      cd Erokin-generator-maker
      mvn package
      ```
    - 然后，启动 `Erokin-generator-web-backend`：
      ```bash
      cd ../Erokin-generator-web-backend
      mvn spring-boot:run
      ```

4.  **启动前端服务**

    - 进入前端项目目录：
      ```bash
      cd ../erokin-generator-web-frontend
      ```
    - 安装依赖：
      ```bash
      npm install
      ```
    - 启动项目：
      ```bash
      npm run dev
      ```

5.  **访问**

    - 前端访问地址：`http://localhost:8000`
    - 后端API文档地址：`http://localhost:8101/api/doc.html`
