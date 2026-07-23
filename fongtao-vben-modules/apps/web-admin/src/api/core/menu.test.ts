import { describe, expect, it } from 'vitest';

import { toAdminMenuRoutes } from './menu';

describe('toAdminMenuRoutes', () => {
  it('转换菜单资源并保留子级关系', () => {
    const routes = toAdminMenuRoutes(
      [
        {
          children: [
            {
              enabled: 1,
              routePath: 'overview',
              sysResCode: 'admin-overview',
              sysResName: '概览',
              sysResType: 'menu',
              visibled: 1,
            },
          ],
          componentPath: 'BasicLayout',
          enabled: 1,
          routePath: '/admin',
          sysResCode: 'admin-root',
          sysResName: '管理',
          sysResType: 'menu',
          visibled: 1,
        },
        {
          enabled: 1,
          sysResCode: 'admin-button',
          sysResName: '按钮',
          sysResType: 'button',
          visibled: 1,
        },
      ],
      (componentPath) => componentPath === 'BasicLayout',
    );

    expect(routes).toHaveLength(1);
    expect(routes[0]).toMatchObject({
      component: 'BasicLayout',
      name: 'admin-root',
      path: '/admin',
    });
    expect(routes[0]?.children?.[0]).toMatchObject({
      component: 'NotFoundView',
      name: 'admin-overview',
      path: 'overview',
    });
  });

  it('没有可用菜单时回退到首页', () => {
    const routes = toAdminMenuRoutes([]);

    expect(routes).toEqual([
      expect.objectContaining({
        name: 'FongtaoHome',
        path: '/dashboard',
      }),
    ]);
  });
});
