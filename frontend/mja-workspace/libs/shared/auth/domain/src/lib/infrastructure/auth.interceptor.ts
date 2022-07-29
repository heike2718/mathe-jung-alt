import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService, STORAGE_KEY_DEV_SESSION_ID } from "@mja-workspace/shared/util-configuration";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {

    constructor(@Inject(SharedConfigService) private config: Configuration) { }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


        // da auf localhost das cookie nicht in den Browser gesetzt und folglich zurückgesendet werden kann,
        // machen wir hier den Umweg über localstorage.
        const sessionId = localStorage.getItem(
            this.config.storagePrefix + STORAGE_KEY_DEV_SESSION_ID
        );
        if (sessionId) {
            let cloned = req.clone({
                headers: req.headers.set('X-SESSIONID', sessionId),
            });
            /*
                  cloned = cloned.clone({
                      headers: req.headers.set('Cache-Control', 'no-cache')
                  });
                  */
            return next.handle(cloned);
        } else {
            return next.handle(req);
        }
    }
}

