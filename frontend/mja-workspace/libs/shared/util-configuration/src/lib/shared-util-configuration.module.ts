import { NgModule, ModuleWithProviders } from "@angular/core";
import { CommonModule } from '@angular/common';
import { Configuration, SharedConfigService } from "./shared.configuration";

@NgModule({
    imports: [
      CommonModule,
    ],
  })
  export class SharedConfigurationModule {
  
    static forRoot(configuration: Configuration): ModuleWithProviders<SharedConfigurationModule> {
  
      return {
        ngModule: SharedConfigurationModule,
        providers: [
          {
            provide: SharedConfigService,
            useValue: configuration
          }
        ]
      }
    }
  }
  