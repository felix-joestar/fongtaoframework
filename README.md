# Fongtao Framework

面向 Java 21 和 Spring Boot 3 的单体业务脚手架。

## 模块

- `fongtao-starter-modules`：可复用基础能力，构建入口为 `pom.xml`。
- `fongtao-starter-dependencies`：内部 BOM，统一管理依赖和 starter 版本。
- `fongtao-starter-parent`：Java、插件、测试和 annotation processor 父工程。
- `fongtao-starter-core`：响应、错误码、分页、链路上下文、校验分组和通用树处理能力。
- `fongtao-website-modules`：业务工程聚合入口。
- `fongtao-admin-starter`：登录、权限和基础数据能力。
- `fongtao-admin-website`：admin starter 本地调试启动工程。

业务工程导入内部 BOM、继承 starter parent，再按需引入 starter。基础 starter 不反向依赖 website；admin starter 是可直接引入的业务基础能力，不要求业务工程实现认证或权限 SPI。

## 配置与数据

- MySQL 基线表结构维护在 `fongtao-website-modules/fongtao-admin-parent/_database_schema/admin-mysql-schema.sql`，应用不会自动执行建表或初始化数据。
- Redis 连接配置使用 `spring.data.redis`；缓存配置使用 `spring.cache.redis`。
- 文件、Forest、Lock4j、Actuator 和 Arthas 使用各自官方配置前缀。
- 数据库、Redis 密码和 JWT secret 使用 `ENC(...)`；Jasypt 主口令仅通过 JVM 系统属性 `jasypt.encryptor.password` 注入。
- Security starter 不提供默认 JWT secret，CORS 默认不开放跨域。

## 编码约定

业务模块按 `controller -> facade -> service -> domain -> mapper` 组织。Facade 负责业务用例和事务，Service 负责本对象数据库操作，Controller 只负责 HTTP 接入和 `R<T>` 响应包装。

对象名称由真实表名转换：`sys_org -> SysOrg -> /sys-org`，`product_order -> ProductOrder -> /product-order`。权限码格式为 `<工程>:<对象 kebab-case>:<动作>`。

实体使用 Lombok，结构化转换使用 MapStruct，通用辅助能力优先使用 Hutool。跨模块树结构使用 core 的 `TreeUtil` 构建不可变 `children`，避免复制递归和循环保护逻辑。MyBatis-Plus 使用 `ASSIGN_UUID` 和 `FieldStrategy.ALWAYS`；更新请求携带 `version`。

## 构建

```text
mvn -f fongtao-starter-modules/pom.xml clean install
mvn -f fongtao-website-modules/pom.xml clean verify
```

按项目规则，只有明确需要时才运行构建、测试或启动应用。
