import { enableProdMode, importProvidersFrom, LOCALE_ID } from '@angular/core';

import { environment } from './environments/environment';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { appRoutes } from './app/app.routes';
import { provideRouter } from '@angular/router';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { Configuration } from '@mja-ws/shared/config';
import { authDataProvider } from '@mja-ws/shared/auth/api';
import { HttpClientModule, HttpClientXsrfModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AddBaseUrlInterceptor } from 'libs/shared/http/src';

if (environment.production) {
  enableProdMode();
}

registerLocaleData(localeDe, 'de');

bootstrapApplication(AppComponent, {
  providers: [
    ...authDataProvider,
    provideAnimations(),
    provideRouter(appRoutes),

    /** das muss so gemacht werden, weil ohne den Parameter {} nichts da ist, wohinein man den state hängen könnte */
    provideStore(),
    provideEffects([]),
    provideStoreDevtools(),

    importProvidersFrom(
      HttpClientModule,
      HttpClientXsrfModule.withOptions({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN'
      })      
    ),
    {
      provide: Configuration,
      useFactory: () => new Configuration(environment.baseUrl, 'ADMIN'),
    },

    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de',
    },
    { provide: LOCALE_ID, useValue: 'de' },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline' },
    },
    { provide: HTTP_INTERCEPTORS, multi: true, useClass: AddBaseUrlInterceptor },
  ],
});
