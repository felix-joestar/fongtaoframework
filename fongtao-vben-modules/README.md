# Fongtao 管理前端

Fongtao 管理前端是面向 Fongtao 单体 admin starter 的 Element Plus 管理端工作区。当前仅提供账号密码登录、JWT 刷新、当前登录用户和后端资源树菜单骨架。

## 工作区

- 应用包：@fongtao/web-admin
- 开发代理：/api -> http://127.0.0.1:8080
- 生产接口：由反向代理提供 /api
- 权限模式：后端资源与权限码驱动

常用命令：

~~~shell
pnpm dev
pnpm typecheck
pnpm build
pnpm lint
~~~

## 后端接口

- POST /auth/login
- POST /auth/refresh-token
- GET /auth/login-user
- GET /auth/login-user/resources
- POST /auth/logout

access token 与 refresh token 均保存在前端登录态中，所有受保护请求使用 Authorization: Bearer token。

## 资源菜单约定

后端 sys_res 的 menu 资源会转换为前端路由：

- sysResCode 用作路由名称。
- routePath 用作路由路径。
- sysResName、icon、visibled 映射到路由 meta。
- componentPath=BasicLayout 表示布局路由。
- 页面组件路径相对于 apps/web-admin/src/views，例如 dashboard/index。
- 找不到页面组件时会回退到现有 404 页面。
- 没有可用菜单时自动进入 Fongtao 首页。

首版不包含 sys-user、sys-org 等管理页面；资源菜单由后端 sys_res 手工维护，后续按业务优先级逐项增加页面。

## 许可

本目录保留上游代码所要求的 MIT License 与必要版权声明。
