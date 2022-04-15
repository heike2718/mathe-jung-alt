import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from '@mathe-jung-alt-workspace/shared/ui-messaging';
import { catchError, Observable, throwError } from 'rxjs';
import { DEFAULT_ERROR_MESSAGE_CONTEXT } from './http.context';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private messageService: MessageService) { }

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((err) => {
        const errorMessageContext = req.context.get(DEFAULT_ERROR_MESSAGE_CONTEXT);
        this.messageService.error(errorMessageContext);
        return throwError(() => err);
      })
    );
  }
}
