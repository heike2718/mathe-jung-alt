import { enableProdMode, importProvidersFrom, LOCALE_ID } from '@angular/core';

import { environment } from './environments/environment';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { appRoutes } from './app/app.routes';
import { provideRouter } from '@angular/router';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { features } from 'process';

if (environment.production) {
  enableProdMode();
}

registerLocaleData(localeDe, 'de');

bootstrapApplication(AppComponent, {
  providers: [
    provideAnimations(),
    provideRouter(appRoutes),

    provideStore(),
    provideEffects([]),
    provideStoreDevtools(),

    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de',
    },    
    { provide: LOCALE_ID, useValue: 'de' },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { appearance: 'outline' },
    },
  ],
});
