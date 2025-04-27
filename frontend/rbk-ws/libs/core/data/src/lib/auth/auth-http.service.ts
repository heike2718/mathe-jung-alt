import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Message } from "@rbk-ws/shared/messaging/api";
import { Observable } from "rxjs";
import { AuthResult, Session } from "@rbk-ws/core/model";

@Injectable({ providedIn: 'root' })
export class AuthHttpService {

    #httpClient = inject(HttpClient);    

    getLoginUrl(): Observable<Message> {

        return this.#httpClient.get<Message>('/raetselbaukasten/api/session/authurls/login');
    }

    getSignupUrl(): Observable<Message> {

        return this.#httpClient.get<Message>('/raetselbaukasten/api/session/authurls/signup');
    }

    createSession(authResult: AuthResult): Observable<Session> {
        return this.#httpClient.post<Session>('/raetselbaukasten/api/session/login', authResult);
    }

    logOut(): Observable<Message> {
        return this.#httpClient.delete<Message>('/raetselbaukasten/api/session/logout');
    }

}