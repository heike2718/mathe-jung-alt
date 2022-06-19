import { HttpClient } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mathe-jung-alt-workspace/shared/configuration";
import { ResponsePayload, Message } from "libs/shared/ui-messaging/src/lib/message/message";
import { map, Observable, of } from "rxjs";
import { AuthResult, Session, STORAGE_KEY_DEV_SESSION_ID } from "../entities/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AuthHttpService {

    #baseUrl = this.configuration.baseUrl + '/session';

    constructor(@Inject(SharedConfigService) private configuration: Configuration, private http: HttpClient) { }


    public getLoginRedirectUrl(): Observable<string> {

        const url = this.#baseUrl + '/authurls/login';

        return this.http.get<Message>(url).pipe(
            map(message => message.message)
        );
    }

    public createSession(authResult: AuthResult): Observable<Session> {

        const url = this.#baseUrl + '/login';
        return this.http.post<Session>(url, authResult);
    }

    public logout(): Observable<Message> {

        const devSessionId = localStorage.getItem(this.configuration.storagePrefix + STORAGE_KEY_DEV_SESSION_ID);
        let url = this.#baseUrl + '/logout';

        if (devSessionId) {
            url = this.#baseUrl + '/dev/logout/' + devSessionId;
        }

        return this.http.delete<Message>(url);
    }
}