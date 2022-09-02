import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService, STORAGE_KEY_SESSION } from "@mja-workspace/shared/util-configuration";
import { Observable } from "rxjs";
import { Session } from "../entities/auth.model";


@Injectable({
    providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {

    constructor(@Inject(SharedConfigService) private config: Configuration) { }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


        // da auf localhost das cookie nicht in den Browser gesetzt und folglich zurückgesendet werden kann,
        // machen wir hier den Umweg über localstorage.
        const sessionId = this.getSessionIdFromLocalStorage();

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

    private getSessionIdFromLocalStorage(): string | undefined {

        const sessionSerialized = localStorage.getItem(STORAGE_KEY_SESSION);
        
        let sessionId = undefined;

        if (sessionSerialized) {
            const session: Session = JSON.parse(sessionSerialized);
            sessionId = session.sessionId;
        }

        return sessionId;
    }
}

