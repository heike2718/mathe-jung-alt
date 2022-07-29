import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Message } from "@mja-workspace/shared/util-mja";
import { Configuration, SharedConfigService, STORAGE_KEY_DEV_SESSION_ID } from "@mja-workspace/shared/util-configuration";
import { map, Observable, tap } from "rxjs";
import { AuthResult, Session } from "../entities/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #csrfHeaderName = 'X-XSRF-TOKEN';

    #baseUrl = this.configuration.baseUrl + '/session';

    constructor(@Inject(SharedConfigService) private configuration: Configuration, private http: HttpClient) { }

    getCsrfToken(): Observable<string | null> {

        const url = this.configuration.baseUrl + '/csrftoken/v1';

        return this.http.get(url, { observe: 'response' }).pipe(
            tap((res: HttpResponse<any>) => console.log('csrf-token=' + res.headers.get(this.#csrfHeaderName))),
            map((res: HttpResponse<any>) => res.headers.get(this.#csrfHeaderName))
        );
    }


    getLoginRedirectUrl(): Observable<string> {

        const url = this.#baseUrl + '/authurls/login';

        return this.http.get<Message>(url).pipe(
            map(message => message.message)
        );
    }

    createSession(authResult: AuthResult): Observable<Session> {

        const url = this.#baseUrl + '/login';
        return this.http.post<Session>(url, authResult);
    }

    logout(): Observable<Message> {

        const devSessionId = localStorage.getItem(this.configuration.storagePrefix + STORAGE_KEY_DEV_SESSION_ID);
        let url = this.#baseUrl + '/logout';

        if (devSessionId) {
            url = this.#baseUrl + '/dev/logout/' + devSessionId;
        }

        return this.http.delete<Message>(url);
    }

    #inspectHeaders(response: any): void {

        if (response instanceof HttpResponse) {
            const httpHeaders = response.headers;
            console.log(httpHeaders.keys);
        }
    }
}
