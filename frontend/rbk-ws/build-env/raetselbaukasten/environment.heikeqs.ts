import { provideStoreDevtools } from '@ngrx/store-devtools';

// export const environment = {
//     production: false,
//     baseUrl: 'http://heikeqs/api',
//     withCredentials: true,
//     assetsPath: '/raetselbaukasten/assets/'
//   };

export const environment = {
  production: false,
  baseUrl: '',
  withCredentials: true,
  assetsPath: '/raetselbaukasten/assets/',
  providers: [provideStoreDevtools({ maxAge: 25, connectInZone: true })],
};
