import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuration } from '@mja-ws/shared/config';

@Injectable()
export class AddBaseUrlInterceptor implements HttpInterceptor {
  
  #config = inject(Configuration);

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (!req.url.startsWith('/')) {
      return next.handle(
        req.clone({
          url: req.url,
          withCredentials: this.#config.withCredentials,
        })
      );
    }
    return next.handle(
      req.clone({
        url: `${this.#config.baseUrl}${req.url}`,
        withCredentials: this.#config.withCredentials,
      })
    );
  }
}
