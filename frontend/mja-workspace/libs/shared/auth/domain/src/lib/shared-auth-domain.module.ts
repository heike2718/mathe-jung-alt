import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { SharedConfigurationModule } from '@mja-workspace/shared/util-configuration';
import { SharedUtilMjaModule } from '@mja-workspace/shared/util-mja';
import { StoreModule } from '@ngrx/store';
import * as fromAuth from './+state/auth.reducer';
import { EffectsModule } from '@ngrx/effects';
import { AuthEffects } from './+state/auth.effects';
import { AuthInterceptor } from './infrastructure/auth.interceptor';
import { AuthGuard } from './infrastructure/auth.guard';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    SharedUtilMjaModule,
    SharedConfigurationModule,
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
        AuthGuard,
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        },
      ]
    }
  }

}
