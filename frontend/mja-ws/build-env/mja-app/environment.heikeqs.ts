import { provideStoreDevtools } from '@ngrx/store-devtools';

// export const environment = {
//     production: false,
//     baseUrl: 'http://heikeqs/mja-api',
//     withCredentials: true,
//     assetsPath: '/mja-app/assets/'
//   };

export const environment = {
  production: false,
  baseUrl: '',
  withCredentials: true,
  assetsPath: '/mja-app/assets/',
  providers: [
    provideStoreDevtools({ maxAge: 25 , connectInZone: true})
  ],
};