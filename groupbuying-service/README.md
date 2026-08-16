# Getting Started

# group buying
# 尚上优选_后端

## 1.启动中间件
### start nacos on windows

### start Sunny-Ngrok on windows
233247428378

###  start the docker on linux
systemctl start docker.service

### # start the container (Redis,ElasticSearch,Kibana,RabbitMQ ):
docker ps


docker run --restart=always --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.153.83:9200 -p 5601:5601 -d kibana:7.4.0

#### 订单微信支付由于没有商户号，暂未测试。

## 2.技术架构
- SpringBoot + SpringCloud + nacos + MyBatis-Plus
- MySQL：存储基础数据
- Redis
  - 微信小程序端登录，登录完成之后，用户信息放到redis
  - 购物车，使用redis的hash类型，存储购物车数据
  - 生成订单，商品锁定库存和其他信息
  - 首页数据，有爆款商品，redis里面zset类型
- Redisson：基于redis的Java驻内存数据网格，实现分布式锁
  - 生成订单，锁定库存使用分布式锁
- RabbitMQ：消息中间件
  - 商品上下架异步处理
  - 生成订单完成，异步通知cart微服务删除购物车数据
  - 支付完成之后，异步通知order微服务更新订单状态、product微服务扣减库存
- ElasticSearch +Kibana: 全文检索服务器 +可视化数据监控
  - 存放sku信息
  - 存放热销产品
  - 通过SpringData操作ElasticSearch

- ThreadPoolExecutor：线程池来实现异步操作，提高效率
  - 商品详情页，使用多线程方式异步获取
- OSS：文件存储服务
    - 存储商品图片
- Knife4j（Swagger）：Api接口文档工具

- Nginx：负载均衡

- uniapp：微信小程序与微信支付


