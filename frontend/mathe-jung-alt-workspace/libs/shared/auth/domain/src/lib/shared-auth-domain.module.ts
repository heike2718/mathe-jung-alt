import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './infrastructure/auth.interceptor';
import { StoreModule } from '@ngrx/store';
import * as fromAuth from './+state/auth.reducer';
import { EffectsModule } from '@ngrx/effects';
import { AuthEffects } from './+state/auth.effects';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    StoreModule.forFeature(
      fromAuth.AUTH_FEATURE_KEY,
      fromAuth.reducer
    ),
    EffectsModule.forFeature([AuthEffects]),
  ],
})
export class SharedAuthDomainModule {

  static forRoot(): ModuleWithProviders<SharedAuthDomainModule> {

    return {
      ngModule: SharedAuthDomainModule,
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        },
      ]
    }
  }
}
