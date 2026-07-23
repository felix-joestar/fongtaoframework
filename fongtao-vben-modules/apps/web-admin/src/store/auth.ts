import type { Recordable, UserInfo } from '@vben/types';

import { ref } from 'vue';
import { useRouter } from 'vue-router';

import { LOGIN_PATH } from '@vben/constants';
import { preferences } from '@vben/preferences';
import { resetAllStores, useAccessStore, useUserStore } from '@vben/stores';

import { ElNotification } from 'element-plus';
import { defineStore } from 'pinia';

import {
  getLoginUserApi,
  loginApi,
  logoutApi,
  toUserInfo,
} from '#/api';
import { $t } from '#/locales';

export const useAuthStore = defineStore('auth', () => {
  const accessStore = useAccessStore();
  const userStore = useUserStore();
  const router = useRouter();

  const loginLoading = ref(false);

  async function authLogin(
    params: Recordable<any>,
    onSuccess?: () => Promise<void> | void,
  ) {
    let userInfo: null | UserInfo = null;
    try {
      loginLoading.value = true;
      const loginResult = await loginApi({
        password: params.password,
        username: params.username,
      });

      accessStore.setAccessToken(loginResult.accessToken);
      accessStore.setRefreshToken(loginResult.refreshToken);
      userInfo = await fetchUserInfo();

      if (accessStore.loginExpired) {
        accessStore.setLoginExpired(false);
      } else if (onSuccess) {
        await onSuccess();
      } else {
        await router.push(
          userInfo.homePath || preferences.app.defaultHomePath,
        );
      }

      if (userInfo.realName) {
        ElNotification({
          message: $t('authentication.loginSuccessDesc') + ':' + userInfo.realName,
          title: $t('authentication.loginSuccess'),
          type: 'success',
        });
      }
    } finally {
      loginLoading.value = false;
    }

    return { userInfo };
  }

  async function logout(redirect: boolean = true) {
    try {
      await logoutApi();
    } catch {
      // 无状态退出失败时仍清理浏览器侧登录态。
    }

    resetAllStores();
    accessStore.setLoginExpired(false);

    await router.replace({
      path: LOGIN_PATH,
      query: redirect
        ? { redirect: encodeURIComponent(router.currentRoute.value.fullPath) }
        : {},
    });
  }

  async function fetchUserInfo() {
    const loginUser = await getLoginUserApi();
    const userInfo = toUserInfo(loginUser, accessStore.accessToken || '');
    userStore.setUserInfo(userInfo);
    accessStore.setAccessCodes(loginUser.permissions);
    return userInfo;
  }

  function $reset() {
    loginLoading.value = false;
  }

  return {
    $reset,
    authLogin,
    fetchUserInfo,
    loginLoading,
    logout,
  };
});
