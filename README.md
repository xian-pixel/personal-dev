# personal_detail

个人详情服务（personal_detail）后端项目，基于 Spring Boot 3 + MyBatis-Plus 的多模块 Maven 工程。

## 技术栈

- **Java 17**
- **Spring Boot 3.0.2**
- **MyBatis-Plus 3.5.0**（ORM）
- **MySQL**（数据持久化，驱动 `mysql-connector-j`）
- **Redis**（缓存，Spring Data Redis）
- **阿里云 OSS**（对象存储）

## 模块结构

```
personal_detail/
├── pom.xml                 # 聚合父 POM，统一管理依赖版本（dependencyManagement 引入 Spring Boot BOM）
├── personal-common/        # 公共模块（工具类、公共依赖等）
└── personal-system/        # 系统主模块（Spring Boot 启动入口、业务代码）
    └── src/main/java/com/zhengyang/PersonalDetailApplication.java
```

## 环境要求

- JDK 17
- Maven 3.6+
- 本地 MySQL（默认库名 `personal_dev`，端口 3306）
- 本地 Redis（默认端口 6379）

## 配置说明

主配置文件：`personal-system/src/main/resources/application.yml`

敏感信息（数据库密码、Redis 密码、OSS AccessKey 等）通过 `application-keys.yml` 注入，并在 `application.yml` 中通过 `spring.config.import` 引入。**请勿将真实密钥提交到版本库。**

`application-keys.yml` 示例：

```yaml
keys:
  db-username: "root"
  db-password: "your_password"
  redis-password: "your_redis_password"
  oss-access-key-id: "your_access_key_id"
  oss-access-key-secret: "your_access_key_secret"
  oss-bucket-name: "your-bucket-name"
```

主要配置项：
- 服务端口：`server.port=8080`
- MyBatis-Plus：开启下划线转驼峰、逻辑删除字段 `deleted`
- 阿里云 OSS：`endpoint=oss-cn-beijing.aliyuncs.com`

## 构建与运行

```bash
# 打包（跳过测试）
mvn clean package -DskipTests

# 运行
cd personal-system
mvn spring-boot:run
```

启动成功后访问：`http://localhost:8080`

## License

[MIT](./LICENSE)
