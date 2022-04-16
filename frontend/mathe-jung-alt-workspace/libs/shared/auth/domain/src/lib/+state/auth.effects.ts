
import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { noop, Observable, of } from 'rxjs';
import { concatMap, map } from 'rxjs/operators';
import { AuthConfigService, AuthConfiguration } from '../application/auth.configuration';
import { STORAGE_KEY_USER, Session, STORAGE_KEY_DEV_SESSION_ID, STORAGE_KEY_SESSION_EXPIRES_AT, AuthResult } from '../entities/auth.model';
import { AuthHttpService } from '../infrastructure/auth-http.service';
import * as AuthActions from './auth.actions';


@Injectable()
export class AuthEffects {


    constructor(private actions$: Actions,
        @Inject(AuthConfigService) private configuration: AuthConfiguration,
        private authHttpService: AuthHttpService,
        private safeNgrx: SafeNgrxService,
        private router: Router) { }

    requestLoginRedirectUrl$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.requestLoginUrl),
            this.safeNgrx.safeSwitchMap(() =>
                this.authHttpService.getLoginRedirectUrl().pipe(
                    map((authUrl) =>
                        AuthActions.getRedirectLoginUrlSuccess({ authUrl: authUrl })
                    )
                ), 'Der Loginserver ist nicht erreichbar.', noopAction()
            )

        )
    );

    redirectToLogin$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.getRedirectLoginUrlSuccess),
            concatMap((action) =>
                of(action.authUrl).pipe(
                    map((authUrl) => {
                        window.location.href = authUrl;
                        return noopAction();
                    })
                )
            )
        )
    );

    createSession$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.createSession),
            this.safeNgrx.safeSwitchMap((action) =>
                this.internalCreateSession(action.authResult).pipe(
                    map((session: Session) => AuthActions.userLoggedIn({ session: session, url: this.configuration.loginSuccessUrl }))
                ), 'Login fehlgeschlagen', noopAction()
            )
        )
    );

    loginSuccess$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.userLoggedIn),
            concatMap((action) =>
                of({ session: action.session, url: action.url }).pipe(
                    map(({ session, url }) => {

                        this.storeSessionInLocalStorage(session);
                        this.router.navigateByUrl(url);
                        return AuthActions.sessionCreated({ session: session });
                    })
                )
            )
        ));

    private getClearOrRestoreResultAction(session?: Session): Action {

        if (session) {
            return AuthActions.sessionRestored({ session: session });
        }
        return AuthActions.sessionCleared();
    }

    private storeSessionInLocalStorage(session: Session): void {

        const user = session.user;
        localStorage.setItem(this.configuration.storagePrefix + STORAGE_KEY_USER, JSON.stringify(user));
        if (session.sessionId) {
            localStorage.setItem(this.configuration.storagePrefix + STORAGE_KEY_DEV_SESSION_ID, session.sessionId);
        }
        localStorage.setItem(this.configuration.storagePrefix + STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(session.expiresAt));
    }

    private internalCreateSession(authResult: AuthResult): Observable<Session> {

        if (this.configuration.withFakeLogin) {
            return this.authHttpService.createFakeSession();
        }

        return this.authHttpService.createSession(authResult);
    }
}
