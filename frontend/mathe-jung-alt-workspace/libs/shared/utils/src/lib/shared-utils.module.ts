import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { HttpConfiguration, HttpConfigurationService } from './http-utils/http.configuration';

@NgModule({
  imports: [
    CommonModule,
    StoreModule,
  ],
  providers: [
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   multi: true,
    //   useClass: LoadingInterceptor,
    // },
  ],
})
export class SharedUtilsModule {

  static forRoot(configuration: HttpConfiguration): ModuleWithProviders<SharedUtilsModule> {

    return {
      ngModule: SharedUtilsModule,
      providers: [
        {
          provide: HttpConfigurationService,
          useValue: configuration
        }
      ]
    }
  }
}
