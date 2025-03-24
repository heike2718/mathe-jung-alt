import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Message } from "@mja-ws/shared/messaging/api";
import { Observable } from "rxjs";
import { AuthResult, Session } from "@mja-ws/core/model";

@Injectable({ providedIn: 'root' })
export class AuthHttpService {

    #httpClient = inject(HttpClient);

    getLoginUrl(): Observable<Message> {

        return this.#httpClient.get<Message>('/mja-api/session/authurls/login');
    }

    getSignupUrl(): Observable<Message> {

        return this.#httpClient.get<Message>('/mja-api/session/authurls/signup');
    }

    createSession(authResult: AuthResult): Observable<Session> {
        return this.#httpClient.post<Session>('/mja-api/session/login', authResult);
    }

    logOut(): Observable<Message> {
        return this.#httpClient.delete<Message>('/mja-api/session/logout');
    }

}