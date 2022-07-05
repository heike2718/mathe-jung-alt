import { ErrorHandler, LOCALE_ID, NgModule } from '@angular/core';
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
import { RaetselFeatureSearchModule } from '@mja-workspace/raetsel/feature-search';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { ErrorHandlerService } from './error-handler.service';
import { RouterModule } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    NotAuthorizedComponent,
    HomeComponent,
    NavigationComponent,
    LayoutComponent,
    NotFoundComponent,
  ],
  imports: [
    RouterModule,
    AppRoutingModule,   
    BrowserModule,
    BrowserAnimationsModule,
    SharedAuthDomainModule,
    SharedUiComponentsModule,
    SuchfilterDomainModule,
    RaetselFeatureSearchModule,
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
  providers: [
    { provide: ErrorHandler, useClass: ErrorHandlerService },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    // { provide: RouterStateSerializer, useClass: CustomRouterStateSerializer },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
