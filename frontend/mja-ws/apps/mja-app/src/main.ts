import { appConfig } from './app/app.config';

import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig);

/** 
 * 
 * bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor]),
    ),
    provideRouter(APP_ROUTES, 
      withPreloading(PreloadAllModules),
    ),
    provideLogger({ debug: true }, 
          CustomLogFormatter);
  ]
}
 * 
*/
