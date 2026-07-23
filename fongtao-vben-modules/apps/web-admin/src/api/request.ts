import type { RequestClientOptions } from '@vben/request';

import { useAppConfig } from '@vben/hooks';
import { preferences } from '@vben/preferences';
import {
  authenticateResponseInterceptor,
  defaultResponseInterceptor,
  errorMessageResponseInterceptor,
  RequestClient,
} from '@vben/request';
import { useAccessStore } from '@vben/stores';

import { ElMessage } from 'element-plus';

import { useAuthStore } from '#/store';

import { refreshTokenApi } from './core';

const { apiURL } = useAppConfig(import.meta.env, import.meta.env.PROD);

function createRequestClient(baseURL: string, options?: RequestClientOptions) {
  const client = new RequestClient({
    ...options,
    baseURL,
  });

  async function doReAuthenticate() {
    const accessStore = useAccessStore();
    const authStore = useAuthStore();
    accessStore.setAccessToken(null);
    if (
      preferences.app.loginExpiredMode === 'modal' &&
      accessStore.isAccessChecked
    ) {
      accessStore.setLoginExpired(true);
      return;
    }
    await authStore.logout();
  }

  async function doRefreshToken() {
    const accessStore = useAccessStore();
    const refreshToken = accessStore.refreshToken;
    if (!refreshToken) {
      throw new Error('缺少 refresh token');
    }

    const loginResult = await refreshTokenApi(refreshToken);
    accessStore.setAccessToken(loginResult.accessToken);
    accessStore.setRefreshToken(loginResult.refreshToken);
    accessStore.setAccessCodes(loginResult.user.permissions);
    return loginResult.accessToken;
  }

  function formatToken(token: null | string) {
    return token ? 'Bearer ' + token : null;
  }

  client.addRequestInterceptor({
    fulfilled: async (config) => {
      const accessStore = useAccessStore();
      const skipAuthorization =
        config.headers['X-Fongtao-Skip-Authorization'] === 'true';

      delete config.headers['X-Fongtao-Skip-Authorization'];
      config.headers.Authorization = skipAuthorization
        ? undefined
        : formatToken(accessStore.accessToken);
      config.headers['Accept-Language'] = preferences.app.locale;
      return config;
    },
  });

  client.addResponseInterceptor(
    defaultResponseInterceptor({
      codeField: 'code',
      dataField: 'data',
      successCode: 0,
    }),
  );

  client.addResponseInterceptor(
    authenticateResponseInterceptor({
      client,
      doReAuthenticate,
      doRefreshToken,
      enableRefreshToken: preferences.app.enableRefreshToken,
      formatToken,
    }),
  );

  client.addResponseInterceptor(
    errorMessageResponseInterceptor((message: string, error) => {
      const responseData = error?.response?.data ?? {};
      const errorMessage = responseData?.message ?? '';
      ElMessage.error(errorMessage || message);
    }),
  );

  return client;
}

export const requestClient = createRequestClient(apiURL, {
  responseReturn: 'data',
});
