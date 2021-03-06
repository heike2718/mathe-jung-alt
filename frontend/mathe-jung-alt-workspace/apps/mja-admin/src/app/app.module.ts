import { NgModule, CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { EffectsModule } from '@ngrx/effects';

import { AppComponent } from './app.component';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './+state';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { AppComponentModule } from './app.component.module';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { SharedAuthDomainModule } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { SharedConfigurationModule } from '@mathe-jung-alt-workspace/shared/configuration';
import { ErrorHandlerService } from './core/error-handler.service';


@NgModule({
  declarations: [HomeComponent],
  imports: [
    // AppComponentModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    EffectsModule.forRoot([]),
    StoreModule.forRoot(reducers, { metaReducers }),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    // NoopAnimationsModule, // dies verhindert das spinnen des loading indicators
    SharedConfigurationModule.forRoot({
      baseUrl: environment.apiUrl,
      production: environment.production,
      profileUrl: environment.profileUrl,
      storagePrefix: environment.storageKeyPrefix,
      admin: true
    }),
    SharedAuthDomainModule.forRoot(),
  ],
  providers: [
    // I0014: besser nicht den LoadingInterceptor verwenden, sondern den Service gezielt einsetzen.
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   multi: true,
    //   useClass: LoadingInterceptor,
    // },
    { provide: ErrorHandler, useClass: ErrorHandlerService },
    { provide: LOCALE_ID, useValue: "de-DE" },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
