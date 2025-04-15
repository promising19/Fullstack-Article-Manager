# 后台开发环境 README 模板

## 一、环境准备

### 1. 数据库表准备
- 执行 SQL 脚本，创建所需的数据库表结构。

### 2. 创建 Spring Boot 工程并引入依赖
- 创建 Spring Boot 项目。
- `pom.xml` 中继承父项目 `spring-boot-starter-parent`：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>版本号</version>
</parent>
```
- 引入以下依赖：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>版本号</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
</dependencies>
```
- 刷新工程确保依赖生效。

### 3. 配置 MyBatis
- 在 `application.yml` 中配置数据源和 MyBatis：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/数据库名
    username: 用户名
    password: 密码
  mybatis:
    mapper-locations: classpath:mapper/*.xml
    type-aliases-package: com.xxx.pojo
```

### 4. 项目包结构
- 建议使用以下结构：
```
itheima
├── controller
├── service
│   └── impl
├── mapper
├── pojo
├── utils
```
- 示例实体类：`Article.java`, `Category.java`, `User.java`

### 5. 编写启动类
```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## 二、接口开发流程

1. 明确需求
2. 阅读接口文档
3. 分析思路
4. 编写代码
5. 接口测试

---

## 三、模块开发说明

### 1. 用户模块
- 注册
  - 参数校验：Spring Validation 注解 `@Pattern`, `@NotBlank` 等
  - 全局异常处理类 `GlobalExceptionHandler`
- 登录
  - 登录后生成 JWT 令牌
  - 使用拦截器验证令牌，统一权限校验
- 获取用户详情
  - 使用 `ThreadLocal` 保存当前登录用户信息
- 更新用户信息 / 密码 / 头像
  - 分层调用，参数校验

### 2. 文章分类模块
- 新增分类
- 查询分类列表/详情
- 更新/删除分类
  - 使用分组校验提升参数复用性

### 3. 文章管理模块
- 新增文章（自定义参数校验）
- 分页查询文章列表
- 获取/更新/删除文章

### 4. 文件上传模块
- 接收 `MultipartFile` 上传图片
- 返回访问地址
- 阿里云 OSS 上传（需接入 SDK）

### 5. 登录优化（Redis 集成）
- 登录成功后将 token 存入 Redis
- 拦截器中比对 Redis 与请求头中的 token
- 修改密码时清除 Redis 中旧 token

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

---

## 四、SpringBoot 项目部署

- 使用 Maven 插件打包为 `jar`：
```bash
mvn clean package
```
- 服务器运行 jar 包：
```bash
java -jar 项目.jar
```
- 要求服务器已安装 JRE 环境

---

## 五、项目配置方式

- **配置文件方式**：`application.yml`
- **命令行参数**：
```bash
java -jar 项目.jar --server.port=8081
```
- **环境变量方式**：设置系统变量
- **外部配置文件**：jar 同目录下新建 `application.yml`
- **多环境配置**：使用 `spring.profiles.active` 指定运行环境

```yaml
spring:
  profiles:
    active: dev
```

---

## 六、前端环境准备（Vue）

### 1. 安装依赖
- Element Plus
- Axios
- Sass

### 2. 调整目录结构

### 3. 开发流程
1. 搭建页面结构与样式
2. 表单绑定与校验（前端）
3. 接口调用封装
4. 数据展示与交互

表单校验建议同时进行：
- 前端校验提升用户体验
- 后端校验确保安全性和业务正确性

---

## 七、注意事项

- 后端异常统一处理
- 参数校验建议使用注解 + 全局异常处理器
- 日志记录、权限管理建议后期接入
- 前后端接口字段需统一规范

---

> 本项目结构适用于中小型 Spring Boot + Vue 项目
> 可根据实际需求进行组件、服务、权限等模块的拆分与扩展

