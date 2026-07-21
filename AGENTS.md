# fongtaoframework 项目补充规则

- 通用工程纪律、中文输出和验证诚信继承用户级全局 AGENTS；本文件只记录当前单体脚手架项目特有规则。
- 修改本文件前必须先说明调整范围；本项目处于早期脚手架阶段，规则应服务“完整 starter 脚手架 + admin 登录、权限与基础数据能力”，不引入暂未实现的平台化约束。
- 数据库初始化脚本优先放在提供能力的 starter 内，例如 `fongtao-admin-starter/src/main/resources/META-INF/fongtao-admin/`；涉及表结构或初始化数据时，必须同步脚本和测试。

## 中文与文案规则

- 面向用户的文案统一使用简体中文：日志、接口描述、校验、返回、异常提示等展示内容应保持中文口径。
- 业务中文文案必须直接以 UTF-8 简体中文书写，禁止用 `\uXXXX`、HTML entity、拼音或其他转义形式代替中文。
- 技术标识统一英文：代码、数据库、接口、配置、环境变量等技术标识符使用英文，数据库字段保持下划线命名。

## 模块边界规则

- 仓库根目录不保留 Maven 聚合 POM；starter 侧构建入口为 `fongtao-starter-modules/pom.xml`，业务侧构建入口为 `fongtao-website-modules/pom.xml`。
- `fongtao-starter-modules` 是 starter 侧顶层聚合 POM，只承载可复用基础能力，模块命名统一为 `fongtao-starter-*`，不放 dependencyManagement 或 pluginManagement。
- `fongtao-starter-dependencies` 是内部 BOM，集中管理 Spring Boot、第三方依赖和内部 starter 版本；业务工程不得绕过该 BOM 自行漂移基础依赖版本。
- `fongtao-starter-parent` 是所有 starter 和业务聚合模块的父工程，只放 Java 21、编码、Maven 插件、测试插件、annotation processor 和 BOM 导入，不沉淀具体业务能力。
- `fongtao-starter-core` 承载统一响应、分页模型、基础异常和基础工具依赖；不得依赖 Web、数据库或 admin 模块。
- `fongtao-starter-web` 承载 Spring MVC、Jakarta Validation、统一异常、Swagger/OpenAPI 和 JSON 默认配置。
- `fongtao-starter-mybatis` 承载 MyBatis-Plus、Druid、分页插件和 P6Spy dev/test 配置。
- `fongtao-starter-security` 只封装 Spring Security、JWT、CORS、认证失败响应、无状态安全链等通用安全能力；不得依赖业务表、admin 实体或 website 模块。
- `fongtao-starter-cache` 承载 Caffeine 本地缓存、Spring Cache、Spring Data Redis、Redisson、RedisTemplate 和 Redis 序列化基础配置；项目不再保留独立 Redis starter。引入该 starter 后 Redis、RedisTemplate 与 Redisson 默认启用，连接信息统一配置在 `spring.data.redis`，缓存策略统一配置在 `spring.cache.redis`，不得新增 `fongtao.cache.redis` 重复连接配置。
- `fongtao-starter-logging` 承载 trace/request id 处理、敏感信息脱敏和可选请求摘要日志；不得记录完整请求体、响应体、Cookie、Authorization 或 token。
- `fongtao-starter-lock` 基于 `fongtao-starter-cache` 中的 Redisson 基础能力封装 Lock4j Redisson。
- `fongtao-starter-file` 承载 x-file-storage；`fongtao-starter-forest` 承载 Forest HTTP 客户端；`fongtao-starter-observability` 承载 Actuator、Arthas，生产默认关闭 Arthas。
- `fongtao-admin-starter` 承载 admin 登录、权限与基础数据能力，包括用户、组织、角色、资源、角色授权、登录身份、字典、配置、流水号以及登录、刷新 token、退出、当前用户和资源树接口。
- `fongtao-admin-website` 只作为 `fongtao-admin-starter` 的本地调试启动工程，不沉淀通用业务能力。
- starter 禁止反向依赖 website 模块；website 可以依赖 starter。

## 基础编码固化规则

- 实体、DTO、Response、配置属性类和简单值对象优先使用 Lombok 简化样板代码，减少手写 getter、setter、构造器和日志字段。
- 持久化实体类优先使用 `@Getter`、`@Setter`，避免在 Entity 上滥用 `@Data` 导致 equals、hashCode、toString 牵连懒加载、敏感字段或双向引用。
- 构造器注入优先使用 `@RequiredArgsConstructor`，日志字段优先使用 `@Slf4j`；不要为了注入依赖手写重复构造器或手写 logger。
- 字符串、集合、对象判空、日期时间、ID、简单 IO 等辅助逻辑优先复用 Hutool 或现有 starter 工具；禁止为常见工具能力重复新建本地 `*Util`。
- Hutool 只作为通用辅助工具使用；Entity、DTO、VO、Response 之间的结构化转换不得使用 Hutool `BeanUtil` 替代 MapStruct。
- Entity、DTO、VO、Response 之间的结构化转换优先使用 MapStruct；转换器命名为 `*Converter`，避免和 MyBatis `*Mapper` 混淆。
- MapStruct 转换器使用 `@Mapper(componentModel = "spring")`，禁止用反射式 BeanUtils、BeanUtil 或手写大段字段复制做常规对象转换。
- Service 抽象接口统一使用 `I*Service` 命名；实现类放在同级 `impl` 子包，类名使用业务语义名称，不加 `Impl` 后缀。
- MyBatis Mapper 和 MapStruct Converter 不使用 `I` 前缀，分别保持 `*Mapper`、`*Converter` 命名；外部框架 SPI 实现类按框架生态命名，不强制套用 Service 命名规则。
- 业务接口不得直接暴露 Entity；入参和出参使用明确的 DTO、Request、Response 或 VO。
- Java 类型命名不使用 `Fongtao` 前缀；Maven 坐标、包名前缀 `com.fongtaoframework` 和配置前缀 `fongtao.*` 保持不变。
- starter 默认配置必须使用 `@ConfigurationProperties(prefix = "fongtao.<module>")` 承载，配置前缀保持模块边界清晰。
- starter 自动配置必须通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册，并用 `@ConditionalOnMissingBean`、`@ConditionalOnProperty`、`@ConditionalOnClass` 控制装配边界。
- starter 不得通过 `application.yaml` 偷塞应用级默认配置；需要默认值时使用 properties 默认值或受控的环境默认处理。
- 子模块不得自行漂移基础依赖版本；新增第三方依赖必须先进入 `fongtao-starter-dependencies` 统一管理。
- 测试不得依赖真实 Redis、MinIO、外部 HTTP 服务或真实数据库服务；starter 优先使用 `ApplicationContextRunner`，Web/Admin 测试优先使用 MockMvc；涉及 MyBatis 集成时使用隔离的 Testcontainers MySQL。

## 后端编码规则

- 业务 starter 的 Java 根包统一使用 `com.fongtaoframework.starter.<business>`；以 admin 为例，公共能力放在 `com.fongtaoframework.starter.admin.common`，业务模块放在 `com.fongtaoframework.starter.admin.modules.<module>`。
- `common` 只放当前业务 starter 内共享的配置、属性、常量、枚举、自动配置和横向基础能力，不放具体业务用例、Controller、Facade、Service、Mapper 或 Entity。
- `modules` 下按业务能力优先拆包，例如 `modules.auth`、后续 `modules.rights`；每个业务模块内部再组织为 `controller`、`facade`、`service`、`domain`、`mapper`、`converter`，避免按全局技术层跨业务聚集。
- `domain` 下按 `entity`、`dto` 等职责继续分包；Entity 只表示数据库映射，DTO/Request/Response 只用于传输和接口契约。
- Controller 只处理请求接入、参数绑定和响应包装，只依赖 Facade；Facade 编排业务用例、认证上下文和多个 Service，不返回 `R<T>`；Service 负责业务规则和事务边界；Mapper 只负责数据库访问。
- Facade 抽象接口使用 `I*Facade` 命名，实现类放在同级 `facade.impl` 子包且不加 `Impl` 后缀；Facade 不直接访问 Mapper。
- 事务注解只放在 Service 实现类或明确的业务 Service 方法，不放在 Controller 或 Facade。
- 统一响应使用 `R<T>`，成功和业务失败分别使用 `R.success(...)`、`R.failed(...)`，不要在同类接口中新增平行响应壳。
- 参数校验优先使用 `@Validated`、Bean Validation 注解和明确 DTO；错误交给统一异常或 Controller 层响应处理。
- 数据访问优先使用 MyBatis-Plus、Mapper 和 Lambda Wrapper，不在业务代码中拼接字符串 SQL。
- 前后端分离登录默认使用 `Authorization: Bearer <token>`；不创建 HTTP Session，不依赖 Cookie 保存登录态。
- admin 当前阶段实现登录、权限与基础数据管理，允许 admin 管理端使用 `@PreAuthorize` 校验资源权限码；不实现第三方登录、OAuth2 授权服务器或通用数据权限 SQL 过滤器，业务模块自行按当前身份的数据范围附加查询条件。
- 密码必须使用 Spring Security `PasswordEncoder` 校验；生产数据不得使用明文或 `{noop}` 密码。
- 权限与基础数据管理按表对象优先拆分：每个主表独立提供 `Sys*Controller`、`ISys*Facade`、`facade.impl.Sys*Facade`、`ISys*Service`、`service.impl.Sys*Service`、`Sys*Mapper`、`Sys*Converter` 和对象专属 DTO；禁止以 `Rights`、`Basedata`、`Management` 等跨表聚合类型承载管理用例。
- 管理接口根路径使用表名的 kebab-case，例如 `sys_org` 对应 `/sys-org`；所有管理动作使用 `POST`，基础动作统一为 `/page`、`/tree`、`/get-by-id`、`/create`、`/update-by-id`、`/delete-by-id`，主键字段使用对应的 `sys*Id`。
- 权限码统一为 `<工程>:<对象的 kebab-case>:<接口动作>`，资源编码统一为 `<工程>-<对象的 kebab-case>-<接口动作>`；工程标识由各 starter 固定定义，不提供运行时改写。admin starter 固定使用 `admin`，例如 `admin:sys-org:page`、`admin-sys-org-page`；权限动作必须与对应 POST 接口动作一致。
- 管理 DTO 按对象放在 `domain.dto` 与 `domain.dto.param`：分页查询为 `Sys*PageParam`，新增、更新为 `Sys*CreateParam`、`Sys*UpdateParam`，出参为 `Sys*Row`；不要继续使用泛化 `*Command`、`*View` 或跨表 DTO。
- `sys_rights_extra` 是 `sys_rights` 的内部子表，只允许由 `SysRights` 的 Facade 和 Service 维护，不新增独立 Controller 或 Facade。

## 安全与日志规则

- 禁止在响应、前端可解码 token 或日志中暴露密码、token、client-secret、Cookie、完整手机号/邮箱、完整请求体、完整 claims 等敏感信息。
- 日志场景涉及敏感数据时，只能记录脱敏摘要、主键、数量、状态、耗时和异常分类。
- 认证失败、未登录、无权限响应必须返回 JSON，不返回 Spring Security 默认 HTML 页面。

## 验证规则

- 新增行为先写测试并确认失败，再实现生产代码。
- starter 改动优先运行受影响模块 Maven test；跨模块依赖变更后运行 `mvn -f fongtao-starter-modules/pom.xml clean install` 和 `mvn -f fongtao-website-modules/pom.xml clean verify`。
- 文档改动跑 UTF-8 读取、内容扫描和 `git diff --check`。
- 无法执行某项验证时，必须说明原因、影响范围和替代检查结果。
