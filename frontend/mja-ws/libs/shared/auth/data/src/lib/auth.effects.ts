import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable } from '@angular/core';
import { Configuration } from '@mja-ws/shared/config';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { concatMap, map, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { Session } from './internal.model';
import { Message, MessageService } from '@mja-ws/shared/messaging/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);
    #coreFacade = inject(CoreFacade);
    #messageService = inject(MessageService);
    #router = inject(Router);

    constructor(@Inject(Configuration) private configuration: Configuration) { }


    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.request_login_url),
            concatMap(() => this.#httpClient.get<Message>('/session/authurls/login/' + this.configuration.clientType)),
            map((message: Message) => authActions.redirect_to_auth({ authUrl: message.message }))
        );

    });

    redirectToLogin$ = createEffect(() =>

        this.#actions.pipe(
            ofType(authActions.redirect_to_auth),
            concatMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl)
        ), { dispatch: false });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.init_session),
            concatMap(({ authResult }) =>
                this.#httpClient.post<Session>('/session/login/' + this.configuration.clientType, authResult)
            ),
            map((session: Session) => authActions.session_created({ session }))
        );
    });

    sessionCreated$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.session_created),
            tap(() => this.#coreFacade.loadQuelleAngemeldeterAdmin())
        ), { dispatch: false });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.log_out),
            concatMap(() =>
                this.#httpClient.delete<Message>('/session/logout')),
            tap(() => this.#coreFacade.handleLogout()),
            map(() => authActions.logged_out())
        );
    });
}