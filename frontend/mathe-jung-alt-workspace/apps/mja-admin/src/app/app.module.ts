import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
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
import { SharedUtilsModule } from '@mathe-jung-alt-workspace/shared/utils';
import { SharedConfigurationModule } from '@mathe-jung-alt-workspace/shared/configuration';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    AppComponentModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    EffectsModule.forRoot([]),
    StoreModule.forRoot(reducers, { metaReducers }),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    NoopAnimationsModule,
    SharedConfigurationModule.forRoot({
      baseUrl: environment.apiUrl,
      production: environment.production,
      profileUrl: environment.profileUrl,
      storagePrefix: environment.storageKeyPrefix,
      withFakeLogin: environment.withFakeLogin,
      admin: true
    }),
    SharedAuthDomainModule.forRoot(),
    SharedUtilsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
