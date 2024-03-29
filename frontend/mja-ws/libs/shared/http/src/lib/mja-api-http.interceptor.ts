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
import { generateUUID } from '@mja-ws/shared/util';

@Injectable()
export class MjaAPIHttpInterceptor implements HttpInterceptor {

  #config = inject(Configuration);

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    const url = this.#config.baseUrl + req.url;

    const auth = localStorage.getItem('mjaCorellationId');
    
    const correlationId = auth ? auth : generateUUID();

    const headers: HttpHeaders = req.headers.append('X-CLIENT-ID', this.#config.clientId).append('X-CORRELATION-ID', correlationId);

    // console.log('correlationId=' + correlationId);

    return next.handle(
      req.clone({
        headers: headers,
        url: url,
        withCredentials: this.#config.withCredentials,
      })
    );
  }
}
