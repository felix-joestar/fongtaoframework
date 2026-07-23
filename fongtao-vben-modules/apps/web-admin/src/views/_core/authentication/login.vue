<script lang="ts" setup>
import type { VbenFormSchema } from '@vben/common-ui';

import { computed } from 'vue';

import { AuthenticationLogin, z } from '@vben/common-ui';

import { useAuthStore } from '#/store';

defineOptions({ name: 'Login' });

const authStore = useAuthStore();

const formSchema = computed((): VbenFormSchema[] => [
  {
    component: 'VbenInput',
    componentProps: {
      placeholder: '请输入账号',
    },
    fieldName: 'username',
    label: '账号',
    rules: z.string().min(1, { message: '请输入账号' }),
  },
  {
    component: 'VbenInputPassword',
    componentProps: {
      placeholder: '请输入密码',
    },
    fieldName: 'password',
    label: '密码',
    rules: z.string().min(1, { message: '请输入密码' }),
  },
]);
</script>

<template>
  <AuthenticationLogin
    :form-schema="formSchema"
    :loading="authStore.loginLoading"
    @submit="authStore.authLogin"
  />
</template>
