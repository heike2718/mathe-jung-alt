import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, concatMap, map, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { Session } from './internal.model';
import { Message } from '@mja-ws/shared/messaging/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);
    #coreFacade = inject(CoreFacade);
    #router = inject(Router);

    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.request_login_url),
            concatMap(() => this.#httpClient.get<Message>('/mja-api/session/authurls/login')),
            map((message: Message) => authActions.redirect_to_auth({ authUrl: message.message }))
        );

    });

    requestSignupUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.request_signup_url),
            concatMap(() => this.#httpClient.get<Message>('/mja-api/session/authurls/signup')),
            map((message: Message) => authActions.redirect_to_auth({ authUrl: message.message }))
        );

    });

    redirectToAuth$ = createEffect(() =>

        this.#actions.pipe(
            ofType(authActions.redirect_to_auth),
            concatMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl)
        ), { dispatch: false });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.init_session),
            concatMap(({ authResult }) =>
                this.#httpClient.post<Session>('/mja-api/session/login', authResult)
            ),
            map((session: Session) => authActions.session_created({ session }))
        );
    });

    sessionCreated$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.session_created),
            concatMap((action) => of(action.session)),
            tap((session) => {
                if (session.user) {
                    this.#coreFacade.loadQuelleAngemeldeterAdmin(session.user.benutzerart);
                    this.#coreFacade.loadDeskriptoren();
                }
            })
        ), { dispatch: false });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.log_out),
            concatMap(() =>
                this.#httpClient.delete<Message>('/mja-api/session/logout')),
            tap(() => this.#coreFacade.handleLogout()),
            map(() => authActions.logged_out()),
            catchError(() => of(authActions.logged_out()))
        );
    });

    loggedOut$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.logged_out),
            tap(() => this.#router.navigateByUrl('/'))
        ), { dispatch: false });

}