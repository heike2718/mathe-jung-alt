import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable } from '@angular/core';
import { Configuration } from '@mja-ws/shared/config';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of, pipe } from 'rxjs';
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

    constructor(@Inject(Configuration) private configuration: Configuration) { }


    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.request_login_url),
            tap(() => console.log('ofType(authActions.request_login_url)')),
            concatMap(() => this.#httpClient.get<Message>('/session/authurls/login/' + this.configuration.clientType)),
            map((message: Message) => authActions.redirect_to_auth({ authUrl: message.message }))
        );

    });

    redirectToLogin$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.redirect_to_auth),
            tap(() => console.log('ofType(authActions.redirect_to_auth)')),
            concatMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl),
            map(() => createNoopAction())
        );
    });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.init_session),
            tap(() => console.log('ofType(authActions.init_session)')),
            concatMap(({ authResult }) =>
                this.#httpClient.post<Session>('/session/login/' + this.configuration.clientType, authResult)
            ),
            map((session: Session) => authActions.session_created({ session }))
        );
    });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.log_out),
            tap((action) => console.log('ofType(authActions.log_out): ' + action)),
            concatMap(() =>
                this.#httpClient.delete<Message>('/session/logout')),
            map(() => authActions.logged_out())
        );
    });

    loggedOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.logged_out),
            tap((action) => {
                console.log('ofType(authActions.logged_out: ' + action);
                this.#clearSession();
            }),
            map(() => createNoopAction())
        );
    });


    #clearSession(): void {
        console.log('local storage putzen');
    }
}