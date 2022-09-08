import { ErrorHandler, LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MaterialModule, SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';
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
import {
  AuthInterceptor,
  SharedAuthDomainModule,
} from '@mja-workspace/shared/auth/domain';
import { LayoutComponent } from './layout/layout.component';
import { RaetselFeatureSearchModule } from '@mja-workspace/raetsel/feature-search';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { ErrorHandlerService } from './error-handler.service';
import { RouterModule } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { QuellenDomainModule } from '@mja-workspace/quellen/domain';
import { QuellenFeatureSearchModule } from '@mja-workspace/quellen/feature-search';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { StoreDevModules } from './store-config/store-devtools';
import { SidenavComponent } from './navigation/sidenav/sidenav.component';
import { HeaderComponent } from './navigation/header/header.component';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [
    AppComponent,
    NotAuthorizedComponent,
    HomeComponent,
    LayoutComponent,
    NotFoundComponent,
    HeaderComponent,
    SidenavComponent,
  ],
  imports: [
    RouterModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    SharedAuthDomainModule,
    SharedUiComponentsModule,
    SuchfilterDomainModule,
    QuellenDomainModule,
    QuellenFeatureSearchModule,
    RaetselFeatureSearchModule,
    EffectsModule.forRoot([]),
    StoreDevModules,
    StoreModule.forRoot(reducers, { metaReducers }),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    // NoopAnimationsModule, // dies verhindert das spinnen des loading indicators
    SharedConfigurationModule.forRoot({
      baseUrl: environment.apiUrl,
      production: environment.production,
      profileUrl: environment.profileUrl,
      storagePrefix: environment.storageKeyPrefix,
      admin: true,
      clientType: 'ADMIN'
    }),
  ],
  providers: [
    { provide: ErrorHandler, useClass: ErrorHandlerService },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    // { provide: RouterStateSerializer, useClass: CustomRouterStateSerializer },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
