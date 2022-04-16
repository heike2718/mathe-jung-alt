import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthConfigService, AuthConfiguration } from './application/auth.configuration';
import { AuthInterceptor } from './infrastructure/auth.interceptor';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
})
export class SharedAuthDomainModule {

  static forRoot(configuration: AuthConfiguration): ModuleWithProviders<SharedAuthDomainModule> {

    return {
      ngModule: SharedAuthDomainModule,
      providers: [
        {
          provide: AuthConfigService,
          useValue: configuration
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        },
      ]
    }
  }
}
