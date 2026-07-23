import type { UserInfo } from '@vben/types';

import type { AuthApi } from './auth';

import { requestClient } from '#/api/request';

export async function getLoginUserApi() {
  return requestClient.get<AuthApi.LoginUser>('/auth/login-user');
}

export function toUserInfo(
  loginUser: AuthApi.LoginUser,
  token: string,
): UserInfo {
  return {
    avatar: '',
    desc: loginUser.email || loginUser.mobile || '',
    homePath: '/dashboard',
    realName: loginUser.name || loginUser.username,
    roles: [],
    token,
    userId: loginUser.userId,
    username: loginUser.username,
  };
}
