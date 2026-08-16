# 尚上优选

社区团购系统。包含微信小程序、Web 管理后台和 Java 微服务后端。

前端、小程序通过网关 `http://localhost:8200` 访问后端。

## 目录结构

```
groupbuying
├── groupbuying-server      # Java 后端（Spring Cloud）
├── groupbuying-web-admin   # Web 管理后台（Vue 2）
└── groupbuying-app         # 微信小程序（uni-app）
```

## 系统介绍

面向社区团购场景：团长组织、用户下单、商品与活动运营、订单履约与支付。

- 小程序端：登录、首页、商品、购物车、下单、秒杀等
- 管理后台：权限、商品、活动、订单、系统配置等
- 后端：按业务拆成多个微服务，经网关统一对外

## 环境版本

| 环境 | 版本 |
| --- | --- |
| JDK | 1.8 |
| Maven | 3.6+ |
| Spring Boot | 2.3.6.RELEASE |
| Spring Cloud | Hoxton.SR8 |
| Spring Cloud Alibaba | 2.2.2.RELEASE |
| Node.js | 14.x |
| npm | >= 3.0.0 |
| Redis | 5.0.5 |
| ElasticSearch | 7.4.0 |
| Kibana | 7.4.0 |
| RabbitMQ | 3.8 |

## 技术架构

- Spring Boot 2.3.6 + Spring Cloud Hoxton.SR8 + Nacos + MyBatis-Plus 3.4.1
- MySQL：基础数据
- Redis：小程序登录态、购物车、锁库存、首页爆款（zset）等
- Redisson：下单锁库存的分布式锁
- RabbitMQ：商品上下架、清购物车、支付后更新订单/扣库存
- ElasticSearch + Kibana：SKU、热销检索
- ThreadPoolExecutor：商品详情异步聚合
- OSS：商品图片
- Knife4j（Swagger）：接口文档
- uni-app：微信小程序与微信支付

订单微信支付因没有商户号，暂未完整测试。

---

## groupbuying-server（后端）

尚上优选 Java 后端。多模块 Maven 工程，入口在网关 `service_gateway`（端口 **8200**）。

主要模块：

| 模块 | 说明 | 端口 |
| --- | --- | --- |
| service_gateway | 网关 | 8200 |
| service_acl | 权限 | 8201 |
| service_sys | 系统 / 仓库区域 | 8202 |
| service_product | 商品 | 8203 |
| service_activity | 活动 / 优惠券 / 秒杀 | 8204 |
| service_search | 搜索 | 8205 |
| service_user | 用户 / 微信登录 | 8206 |
| service_home | 首页 | 8207 |
| service_cart | 购物车 | 8208 |
| service_order | 订单 | 8209 |
| service_payment | 支付 | 8210 |

SQL 脚本在 `groupbuying-server/sql/`。

微信 AppId、OSS Key 等敏感配置写在各服务的 `application-dev.yml` 中，**不要提交到 GitHub**。仓库里是占位符，本地改成真实值后即可启动。

### 启动中间件

先启动 Nacos。

Linux 上启动 Docker 后，再起 Redis、ElasticSearch、Kibana、RabbitMQ：

```bash
systemctl start docker.service
docker ps
```

Kibana 示例：

```bash
docker run --restart=always --name kibana \
  -e ELASTICSEARCH_HOSTS=http://192.168.153.83:9200 \
  -p 5601:5601 -d kibana:7.4.0
```

同时准备好 MySQL（库名见各服务 `application-dev.yml`）。

### 启动后端

用 IDEA 打开 `groupbuying-server`，Maven 安装依赖后，按业务需要启动各微服务，**最后启动 `service_gateway`**。

本地开发通常需要：Nacos、网关，以及登录相关的 `service_user`、`service_acl`，再按页面补齐 product / cart / order 等。

接口文档：各服务自带 Knife4j，经网关访问。

---

## groupbuying-web-admin（Web 管理后台）

尚上优选 Web 前端。基于 vue-admin-template（Vue 2 + Element UI），默认开发地址 [http://localhost:9528/](http://localhost:9528/)。

接口地址在 `.env.development` 的 `VUE_APP_BASE_API`（开发环境指向 `http://localhost:8200`）。该文件被 gitignore，需本地自行配置。

### 启动

```bash
cd groupbuying-web-admin
npm install
npm run dev
```

浏览器访问：http://localhost:9528/

### 打包

```bash
npm run build:stage
npm run build:prod
```

---

## groupbuying-app（微信小程序）

尚上优选微信小程序端。uni-app + uView，用 HBuilderX 打开 `groupbuying-app`。

请求基址在 `groupbuying-app/common/http.interceptor.js`，开发环境为 `http://127.0.0.1:8200/api`。

微信小程序 AppId 在 `manifest.json` / `project.config.json` 的 `mp-weixin.appid`。

### 启动

1. 用 HBuilderX 打开 `groupbuying-app`
2. 运行到小程序模拟器（或微信开发者工具）
3. 确认后端网关已在 8200 启动，且 `service_user` 本地已配置微信 `app_id` / `app_secret`

依赖如需安装：

```bash
cd groupbuying-app
npm install
```

---

## 建议启动顺序

1. Nacos、MySQL、Redis、RabbitMQ、ElasticSearch
2. 后端微服务 + 网关（8200）
3. Web：`groupbuying-web-admin` → `npm run dev`
4. 小程序：HBuilderX 运行 `groupbuying-app`
