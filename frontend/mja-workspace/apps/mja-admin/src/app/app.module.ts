import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { environment } from '../environments/environment';
import { metaReducers, reducers } from './+state';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { SharedConfigurationModule } from '@mja-workspace/shared/util-configuration';
import { SharedAuthDomainModule } from '@mja-workspace/shared/auth/domain';
import { NavigationComponent } from './navigation/navigation.component';
import { LayoutComponent } from './layout/layout.component';

@NgModule({
  declarations: [
    AppComponent,
    NotAuthorizedComponent,
    HomeComponent,
    NavigationComponent,
    LayoutComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    SharedAuthDomainModule,
    SharedUiComponentsModule,
    EffectsModule.forRoot([]),
    StoreModule.forRoot(reducers, { metaReducers }),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    // NoopAnimationsModule, // dies verhindert das spinnen des loading indicators
    SharedConfigurationModule.forRoot({
      baseUrl: environment.apiUrl,
      production: environment.production,
      profileUrl: environment.profileUrl,
      storagePrefix: environment.storageKeyPrefix,
      admin: true,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
