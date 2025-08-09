import { ApplicationConfig } from '@angular/core';
import { authDataProvider } from '@rbk-ws/core/api';
import {
  coreDeskriptorenDataProvider,
  coreQuelleDataProvider,
  coreStatistikDataProvider,
} from '@rbk-ws/core/api';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouterStore } from '@ngrx/router-store';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import {
  LocalStorageEffects,
  localStorageReducer,
  loggedOutMetaReducer,
} from '@rbk-ws/local-storage-data';
import { environment } from '../environments/environment';
import {
  enableProdMode,
  ErrorHandler,
  LOCALE_ID,
} from '@angular/core';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
  withXsrfConfiguration,
} from '@angular/common/http';
import { Configuration } from '@rbk-ws/shared/config';
import { ErrorInterceptor, RaetselbaukastenAPIHttpInterceptor } from '@rbk-ws/shared/http';
import { LoadingInterceptor } from '@rbk-ws/shared/messaging/api';
import { ErrorHandlerService } from './services/error-handler.service';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import { aufgabensammlungenDataProvider } from '@rbk-ws/aufgabensammlungen/api';
import { raetselDataProvider } from '@rbk-ws/raetsel/api';
import { medienDataProvider } from '@rbk-ws/medien/api';

if (environment.production) {
  enableProdMode();
}

const localStorageMetaReducer = localStorageReducer(); // <-- synchronisiert die im reducer genannten Slices des Store mit localStorage wegen F5.
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
      },
    ),
    provideEffects(LocalStorageEffects),
    environment.providers,
    aufgabensammlungenDataProvider,
    raetselDataProvider,
    medienDataProvider,
    provideHttpClient(
      withInterceptorsFromDi(),
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      }),
    ),
    {
      provide: Configuration,
      useFactory: () =>
        new Configuration(
          environment.baseUrl,
          environment.assetsPath,
          environment.withCredentials,
          'raetselbaukasten',
          environment.production,
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
      useClass: RaetselbaukastenAPIHttpInterceptor,
    },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: ErrorInterceptor },
  ],
};
