import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable } from '@angular/core';
import { Configuration } from '@mja-ws/shared/config';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { concatMap, map, switchMap, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { createNoopAction } from '@mja-ws/shared/ngrx-utils';
import { Session } from './internal.model';
import { MessageService, Message } from '@mja-ws/shared/messaging/api';

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);
    #messageService = inject(MessageService);

    constructor(@Inject(Configuration) private configuration: Configuration) { }


    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.requestLoginUrl),
            switchMap(() => this.#httpClient.get<Message>('/session/authurls/login/' + this.configuration.clientType)),
            map((message: Message) => authActions.redirectToAuthprovider({ authUrl: message.message }))
        );

    });

    redirectToLogin$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.redirectToAuthprovider),
            concatMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl),
            map(() => createNoopAction())
        );
    });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.initSession),
            switchMap(({ authResult }) =>
                this.#httpClient.post<Session>('/session/login/' + this.configuration.clientType, authResult)
            ),
            map((session: Session) => authActions.sessionCreated({ session }))
        );
    });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.logOut),
            switchMap(() =>
                this.#httpClient.delete<Message>('/session/logout')
            ),
            map(() => authActions.loggedOut())
        );
    });

    loggedOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.loggedOut),
            tap(() => this.#clearSession()),
            map(() => createNoopAction())
        );
    })


    #clearSession(): void {
        console.log('local storage putzen');
    }
}