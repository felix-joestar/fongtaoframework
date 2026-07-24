# fongtao-vben-modules 项目补充规则

- 本文件适用于整个前端 Monorepo，继承仓库根目录 `AGENTS.md`；这里只补充前端特有规则，不重复通用工程纪律。
- 当前唯一应用为 Element Plus 管理端 `apps/admin`。新增业务优先落在应用层，不为假设中的第二个应用提前抽取共享能力。

## 目录与模块边界

- `apps/admin` 承载登录、路由、页面、接口适配和业务状态；业务代码沿用现有 Vben 分层：
  - 接口与契约：`src/api/<domain>`。
  - 页面与页面内组件：`src/views/<domain>`。
  - 业务路由：`src/router/routes/modules`。
  - 只有确实跨页面共享的客户端状态才进入 `src/store`，页面局部状态留在页面或 composable 中。
- `packages/*` 只承载真正跨应用复用的能力或框架级根因修复。单个 admin 业务需求不得下沉到共享包。
- 保留现有 `@vben/*`、`@vben-core/*` 包 scope，不在业务任务中顺带进行全仓重命名。
- `internal/*` 和 `scripts/*` 只承载构建、质量检查及仓库工具，不放业务页面、业务接口或业务规则。

## Vue 与组件约定

- 使用 TypeScript、Composition API 和 `<script setup lang="ts">`，沿用现有导入、命名和目录风格。
- 应用内导入优先使用已经配置的 `#/*` 子路径；不要另建重复的 Vite alias，也不要用深层相对路径绕过公开导出。
- 表单、表格和通用交互优先复用 `@vben/common-ui`、`apps/admin/src/adapter`、`useVbenForm` 和 `useVbenVxeGrid`。
- 现有适配层确实缺少能力时才直接使用 Element Plus；不得为已有能力引入第二套 UI、表单或表格依赖。
- 业务对象的字段、状态和校验规则以对应后端 DTO 为准，不从演示页面或 Mock 数据反推契约。

## 服务端交互与认证

- 同仓库
  `fongtao-website-modules/fongtao-admin-parent/fongtao-admin-starter`
  中的 Controller、请求/响应 DTO、`R<T>`、资源树和权限码是 admin 业务接口的事实来源。
- 修改接口前必须先读取对应 Controller、DTO 和权限注解；不得照搬 Vben Mock 的 URL、字段或成功码。
- 请求统一经过 `apps/admin/src/api/request.ts` 和 `@vben/request`。业务模块不得直接创建 Axios 实例、重复实现拦截器或自行解析另一套响应壳。
- 登录态使用 `Authorization: Bearer <token>`。access token 与 refresh token 只通过现有 access store 和请求拦截器管理。
- 页面和业务模块不得新增 Cookie、自定义 token 缓存或独立 `localStorage` 键；退出登录必须复用现有清理流程。
- token、Authorization、Cookie、密码和完整认证响应不得输出到日志、提示或错误上报中。
- `VITE_*` 会进入客户端产物，一律视为公开配置；密钥、数据库口令和服务端凭据不得写入前端环境文件。

## 路由、菜单与权限

- 采用混合路由：
  - 登录、错误兜底等核心路由以及 dashboard/home 由前端固定。
  - 用户、组织、权限、基础数据等业务菜单和路由由后端资源树驱动。
- 不为同一业务入口同时维护一份静态菜单和一份后端菜单。后端资源字段需要转换时，在 admin 应用的访问控制边界集中适配。
- 顶级路由使用绝对路径，子路由使用相对路径；页面组件必须通过现有路由视图映射加载，不拼接不受控的动态 import。
- 按钮和操作权限只以后端返回的 permission code 为依据，复用现有 access API 或指令控制展示、禁用。
- 业务代码不得硬编码角色名判断权限。前端权限控制只改善交互，后端鉴权始终是最终安全边界。

## 文案、国际化与偏好设置

- 新增业务界面默认使用简体中文，但必须保留现有 i18n 机制。
- admin 业务文案放在 `apps/admin/src/locales/langs`；只有真正属于共享包的通用文案才进入共享语言包。
- 不在模板、脚本和语言包之间重复维护同一业务文案，也不为未要求的语言补占位翻译。
- 应用偏好只在 `apps/admin/src/preferences.ts` 覆盖确需改变的字段；保留 `fongtao-admin` namespace，避免与其他应用的本地数据冲突。
- 纯共享包不得耦合 `import.meta.env` 或应用专属运行时配置；应用配置通过既有公开接口传入。

## 依赖与工程工具

- 只使用 pnpm。内部 workspace 依赖使用 `workspace:*`，共享外部依赖版本使用 `pnpm-workspace.yaml` 中的 catalog。
- 优先复用现有依赖；新增依赖前先确认标准 Web API、Vue/Vben 能力和当前依赖均不能满足需求。
- 依赖或 workspace 发生变化时才更新 `pnpm-lock.yaml`，不得手工编辑 lockfile 或 `node_modules`。
- 使用仓库现有 Oxfmt、Oxlint、ESLint、Stylelint、Cspell、Commitlint、Lefthook 和 Turbo 配置，不新增平行工具链。
- Windows 下重命名 workspace 前先处理目录内的 `node_modules` 链接；不要用 `Move-Item` 展开或搬运 pnpm junction。
- Tailwind 配置沿用 `internal/tailwind-config` 和 `internal/vite-config`，不要新增旧式 `tailwind.config.*`。

## 当前过渡内容

- 仓库仍可能包含演示页面、Vben 品牌文案和 Mock 风格接口等上游过渡内容。
- 新业务不得复制或扩展这些内容，也不得把它们当成后端契约。
- 只有用户明确要求清理时才删除过渡内容；普通功能任务不得顺手进行全仓品牌、演示或目录清理。

## 验证规则

- 默认执行与改动直接相关的内容、配置、格式和 lint 检查，并运行 `git diff --check`。
- 只有用户明确要求时才运行 typecheck、单元测试、完整 build 或启动服务；未运行时必须说明验证边界。
- 文档改动至少进行 UTF-8 读取、关键内容扫描、链接格式检查和 `git diff --check`。
- 验证命令必须从 `fongtao-vben-modules` 根目录执行，优先使用根 `package.json` 已有脚本和 pnpm filter，不自行拼装另一套流程。

## 官方参考

- [Vben Admin 文档](https://doc.vben.pro/)
- [目录说明](https://doc.vben.pro/guide/project/dir.html)
- [项目规范](https://doc.vben.pro/guide/project/standard.html)
- [路由](https://doc.vben.pro/guide/essentials/route.html)
- [服务端交互](https://doc.vben.pro/guide/essentials/server.html)
- [权限](https://doc.vben.pro/guide/in-depth/access.html)
