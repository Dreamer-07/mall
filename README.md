## mall

### 学习文档

项目源代码中的 `docs/` 目录下

### 项目介绍

`mall` 项目是一套基于 B2C 模式的电商系统，包括前台商城系统及后台管理系统，使用 SpringCloud + Vue 实现，采用 Kubesphre 实现容器化部署

### 项目结构

```
mall
|-- mall-coupon		优惠券服务
|-- mall-member		用户服务
|-- mall-order		订单服务
|-- mall-product	产品服务
|-- mall-ware		存储服务
```

### 技术选型

#### 后端技术

| 技术        | 说明           | 版本                             |
| ----------- | -------------- | -------------------------------- |
| SpringBoot  | 微服务模块开发 | 2.3.11.RELEASE                   |
| OpenFeign   | 远程调用       | SpringCloud.Hoxton.SR9           |
| Nacos       | 微服务注册中心 | SpringCloudAlibaba.2.2.3.RELEASE |
| MyBatisPlus | ORM 框架       | 3.4.2                            |
| Mysql       | 数据库         | 8.0.17                           |
| Lombok      | Java 工具库    | 1.16.18                          |

#### 前端技术

