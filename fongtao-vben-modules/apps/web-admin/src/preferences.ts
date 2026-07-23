import {
  appCopyrightPreferences,
  defineOverridesPreferences,
} from '@vben/preferences';

export const overridesPreferences = defineOverridesPreferences({
  app: {
    accessMode: 'backend',
    defaultHomePath: '/dashboard',
    name: import.meta.env.VITE_APP_TITLE,
  },
  copyright: appCopyrightPreferences,
});
