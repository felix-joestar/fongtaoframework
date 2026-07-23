<script lang="ts" setup>
import { computed } from 'vue';

import { AuthenticationLoginExpiredModal } from '@vben/common-ui';
import { BasicLayout, LockScreen, UserDropdown } from '@vben/layouts';
import { useAccessStore, useUserStore } from '@vben/stores';

import { useAuthStore } from '#/store';
import LoginForm from '#/views/_core/authentication/login.vue';

const accessStore = useAccessStore();
const authStore = useAuthStore();
const userStore = useUserStore();

const avatar = computed(() => userStore.userInfo?.avatar || '');

async function handleLogout() {
  await authStore.logout(false);
}
</script>

<template>
  <BasicLayout
    :avatar
    :text="userStore.userInfo?.realName"
    @clear-preferences-and-logout="handleLogout"
    @logout="handleLogout"
  >
    <template #user-dropdown>
      <UserDropdown
        :avatar
        :menus="[]"
        :text="userStore.userInfo?.realName"
        @clear-preferences-and-logout="handleLogout"
        @logout="handleLogout"
      />
    </template>
    <template #extra>
      <AuthenticationLoginExpiredModal
        v-model:open="accessStore.loginExpired"
        :avatar
      >
        <LoginForm />
      </AuthenticationLoginExpiredModal>
    </template>
    <template #lock-screen>
      <LockScreen :avatar @to-login="handleLogout" />
    </template>
  </BasicLayout>
</template>
