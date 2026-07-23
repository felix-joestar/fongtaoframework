import type { RouteRecordStringComponent } from '@vben/types';

import { requestClient } from '#/api/request';

export interface SysResRow {
  children?: SysResRow[];
  componentPath?: string;
  enabled: number;
  icon?: string;
  routePath?: string;
  sysResCode: string;
  sysResName: string;
  sysResType: string;
  visibled: number;
}

const pageComponents = import.meta.glob('../../views/**/*.vue');

const fongtaoHomeRoute: RouteRecordStringComponent = {
  component: 'dashboard/index',
  meta: {
    icon: 'lucide:house',
    order: -1,
    title: '首页',
  },
  name: 'FongtaoHome',
  path: '/dashboard',
};

export async function getAllMenusApi() {
  const resources =
    await requestClient.get<SysResRow[]>('/auth/login-user/resources');
  return toAdminMenuRoutes(resources);
}

export function toAdminMenuRoutes(
  resources: SysResRow[],
  componentExists = (componentPath: string) =>
    componentPath === 'BasicLayout' ||
    Boolean(pageComponents['../../views/' + componentPath + '.vue']),
): RouteRecordStringComponent[] {
  const routes = resources
    .filter((resource) => resource.sysResType === 'menu' && resource.enabled === 1)
    .map((resource) => toRoute(resource, componentExists));

  return routes.length > 0 ? routes : [fongtaoHomeRoute];
}

function toRoute(
  resource: SysResRow,
  componentExists: (componentPath: string) => boolean,
): RouteRecordStringComponent {
  const component = resolveComponentPath(resource.componentPath, componentExists);
  const children = (resource.children ?? [])
    .filter((child) => child.sysResType === 'menu' && child.enabled === 1)
    .map((child) => toRoute(child, componentExists));

  return {
    children,
    component,
    meta: {
      hideInMenu: resource.visibled !== 1,
      icon: resource.icon,
      title: resource.sysResName,
    },
    name: resource.sysResCode,
    path: resource.routePath || resource.sysResCode,
  };
}

function resolveComponentPath(
  componentPath: string | undefined,
  componentExists: (componentPath: string) => boolean,
) {
  if (!componentPath) {
    return 'NotFoundView';
  }
  if (componentPath === 'BasicLayout') {
    return componentPath;
  }

  const normalized = componentPath
    .replace(/^\/?src\/views\//, '')
    .replace(/\.vue$/, '');

  return componentExists(normalized) ? normalized : 'NotFoundView';
}
