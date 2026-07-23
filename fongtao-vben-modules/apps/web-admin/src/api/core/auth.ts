import { requestClient } from '#/api/request';

export namespace AuthApi {
  export interface LoginParams {
    password: string;
    username: string;
  }

  export interface LoginUser {
    avatarFileId?: string;
    email?: string;
    mobile?: string;
    name: string;
    permissions: string[];
    userId: string;
    username: string;
  }

  export interface LoginResult {
    accessToken: string;
    accessTokenExpiresIn: number;
    refreshToken: string;
    refreshTokenExpiresIn: number;
    user: LoginUser;
  }
}

export async function loginApi(data: AuthApi.LoginParams) {
  return requestClient.post<AuthApi.LoginResult>('/auth/login', data);
}

export async function refreshTokenApi(refreshToken: string) {
  return requestClient.post<AuthApi.LoginResult>(
    '/auth/refresh-token',
    { refreshToken },
    {
      headers: {
        'X-Fongtao-Skip-Authorization': 'true',
      },
    },
  );
}

export async function logoutApi() {
  return requestClient.post('/auth/logout');
}
