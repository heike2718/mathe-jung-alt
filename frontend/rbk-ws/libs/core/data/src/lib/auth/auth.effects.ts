import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, switchMap, map, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { Message } from '@rbk-ws/shared/messaging/api';
import { Router } from '@angular/router';
import { Session } from '@rbk-ws/core/model';
import { AuthHttpService } from './auth-http.service';

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #router = inject(Router);
    #authHttpService = inject(AuthHttpService);

    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.rEQUEST_LOGIN_URL),
            switchMap(() => this.#authHttpService.getLoginUrl()),
            map((message: Message) => authActions.rEDIRECT_TO_AUTH({ authUrl: message.message }))
        );

    });

    requestSignupUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.rEQUEST_SIGNUP_URL),
            switchMap(() => this.#authHttpService.getSignupUrl()),
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
                this.#authHttpService.createSession(authResult)
            ),
            map((session: Session) => authActions.sESSION_CREATED({ session }))
        );
    });

    logOut$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.lOG_OUT),
            switchMap(() =>
                this.#authHttpService.logOut()),
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