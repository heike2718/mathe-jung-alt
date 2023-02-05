import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuration } from '@mja-ws/shared/config';
import { Session } from 'libs/shared/auth/data/src/lib/internal.model';
import { AuthState } from 'libs/shared/auth/data/src/lib/auth.reducer';
import { generateUUID } from '@mja-ws/shared/util';

@Injectable()
export class AddBaseUrlInterceptor implements HttpInterceptor {

  #config = inject(Configuration);

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    let url = req.url;
    if (url.startsWith('/')) {
      url = `${this.#config.baseUrl}${req.url}`;
    }

    let authState: AuthState | undefined = undefined;
    const auth = localStorage.getItem('auth');
    if (auth) {
      authState = JSON.parse(auth);
    }

    const correlationId = (authState && authState.correlationId) ? authState.correlationId : generateUUID();

    const headers: HttpHeaders = req.headers.append('X-CLIENT-ID', this.#config.clientId).append('X-CORRELATION-ID', correlationId);

    console.log('correlationId=' + correlationId);

    return next.handle(
      req.clone({
        headers: headers,
        url: url,
        withCredentials: this.#config.withCredentials,
      })
    );
  }
}
