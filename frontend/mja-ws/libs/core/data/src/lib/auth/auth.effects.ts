import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, switchMap, map, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { Session } from './internal.model';
import { Message } from '@mja-ws/shared/messaging/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Router } from '@angular/router';
import { BENUTZERART } from '@mja-ws/core/model';

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
            ofType(authActions.rEQUEST_LOGIN_URL),
            switchMap(() => this.#httpClient.get<Message>('/mja-api/session/authurls/login')),
            map((message: Message) => authActions.rEDIRECT_TO_AUTH({ authUrl: message.message }))
        );

    });

    requestSignupUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.rEQUEST_SIGNUP_URL),
            switchMap(() => this.#httpClient.get<Message>('/mja-api/session/authurls/signup')),
            map((message: Message) => authActions.rEDIRECT_TO_AUTH({ authUrl: message.message }))
        );

    });

    redirectToAuth$ = createEffect(() =>

        this.#actions.pipe(
            ofType(authActions.rEDIRECT_TO_AUTH),
            switchMap((action) => of(action.authUrl)),
            tap((authUrl) => window.location.href = authUrl)
        ), { dispatch: false });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.iNIT_SESSION),
            switchMap(({ authResult }) =>
                this.#httpClient.post<Session>('/mja-api/session/login', authResult)
            ),
            map((session: Session) => authActions.sESSION_CREATED({ session }))
        );
    });

    sessionCreated$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.sESSION_CREATED),
            switchMap((action) => of(action.session)),
            tap((session) => {
                if (session.user) {

                    const benutzerart: BENUTZERART = session.user.benutzerart;

                    if (benutzerart === 'ADMIN' || benutzerart === 'AUTOR') {
                        this.#coreFacade.loadAutor();
                    }                    
                    this.#coreFacade.loadDeskriptoren();
                }
            })
        ), { dispatch: false });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.lOG_OUT),
            switchMap(() =>
                this.#httpClient.delete<Message>('/mja-api/session/logout')),
            tap(() => this.#coreFacade.handleLogout()),
            map(() => authActions.lOGGED_OUT()),
            catchError(() => of(authActions.lOGGED_OUT()))
        );
    });

    loggedOut$ = createEffect(() =>
        this.#actions.pipe(
            ofType(authActions.lOGGED_OUT),
            tap(() => this.#router.navigateByUrl('/'))
        ), { dispatch: false });

}