import { ApplicationConfig } from '@angular/core';
import { authDataProvider } from '@mja-ws/shared/auth/api';
import {
  coreDeskriptorenDataProvider,
  coreQuelleDataProvider,
  coreStatistikDataProvider,
} from '@mja-ws/core/api';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouterStore } from '@ngrx/router-store';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { provideEffects } from '@ngrx/effects';
import {
  LocalStorageEffects,
  localStorageReducer,
  loggedOutMetaReducer,
} from '@mja-ws/local-storage-data';
import { environment } from '../environments/environment';
import {
  enableProdMode,
  ErrorHandler,
  importProvidersFrom,
  // isDevMode,
  LOCALE_ID,
} from '@angular/core';
import {
  HttpClientModule,
  HttpClientXsrfModule,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Configuration } from '@mja-ws/shared/config';
import { ErrorInterceptor, MjaAPIHttpInterceptor } from '@mja-ws/shared/http';
import { LoadingInterceptor } from '@mja-ws/shared/messaging/api';
import { ErrorHandlerService } from './services/error-handler.service';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { embeddableImagesDataProvider } from '@mja-ws/embeddable-images/api';

if (environment.production) {
  enableProdMode();
}

const localStorageMetaReducer = localStorageReducer(
  'mjaAuth',
  'mjaCoreQuelle',
  'mjaCoreDeskriptoren'
); // <-- synchronisiert diese Slices des Store mit localStorage wegen F5.
const clearStoreMetaReducer = loggedOutMetaReducer;

const allMetaReducers = environment.production
  ? [localStorageMetaReducer]
  : [localStorageMetaReducer, clearStoreMetaReducer];

registerLocaleData(LOCALE_ID, 'de');

export const appConfig: ApplicationConfig = {
  providers: [
    ...authDataProvider,
    coreQuelleDataProvider,
    coreDeskriptorenDataProvider,
    coreStatistikDataProvider,
    embeddableImagesDataProvider,
    provideAnimations(),
    provideRouter(appRoutes),
    provideRouterStore(),

    /** das muss so gemacht werden, weil ohne den Parameter {} nichts da ist, wohinein man den state hängen könnte */
    provideStore(
      {
        // router: routerReducer,
      },
      {
        metaReducers: allMetaReducers,
      }
    ),
    provideEffects(LocalStorageEffects),
    environment.providers,
    importProvidersFrom(
      HttpClientModule,
      HttpClientXsrfModule.withOptions({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      })
    ),
    {
      provide: Configuration,
      useFactory: () =>
        new Configuration(
          environment.baseUrl,
          environment.assetsPath,
          environment.withCredentials,
          'mja-app',
          environment.production
        ),
    },

    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE',
    },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline', floatLabel: 'auto' },
    },
    { provide: ErrorHandler, useClass: ErrorHandlerService },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: LoadingInterceptor },
    {
      provide: HTTP_INTERCEPTORS,
      multi: true,
      useClass: MjaAPIHttpInterceptor,
    },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: ErrorInterceptor },
  ],
};
