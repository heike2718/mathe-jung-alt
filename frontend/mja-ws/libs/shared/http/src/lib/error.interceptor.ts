import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
  } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { catchError, Observable, throwError } from 'rxjs';
  
  @Injectable()
  export class ErrorInterceptor implements HttpInterceptor {
  
   intercept(
      req: HttpRequest<unknown>,
      next: HttpHandler
    ): Observable<HttpEvent<unknown>> {
      return next.handle(req).pipe(
        catchError((err) => {          
          return throwError(() => err);
        })
      );
    }
  }
  