
import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Configuration, SharedConfigService, STORAGE_KEY_SESSION } from '@mja-workspace/shared/util-configuration';
import { MessageService, SafeNgrxService, noopAction } from '@mja-workspace/shared/util-mja';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { concatMap, map, switchMap, tap } from 'rxjs/operators';
import { Session, User } from '../entities/auth.model';
import { AuthHttpService } from '../infrastructure/auth-http.service';
import * as AuthActions from './auth.actions';


@Injectable()
export class AuthEffects {

    #storagePrefix!: string;


    constructor(private actions$: Actions,
        @Inject(SharedConfigService) private configuration: Configuration,
        private authHttpService: AuthHttpService,
        private messageService: MessageService,
        private safeNgrx: SafeNgrxService,
        private router: Router) {

        this.#storagePrefix = this.configuration.storagePrefix;
    }

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
                this.authHttpService.createSession(action.authResult).pipe(
                    map((session: Session) => AuthActions.userLoggedIn({ session: session }))
                ), 'Login fehlgeschlagen', noopAction()
            )
        )
    );

    loginSuccess$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.userLoggedIn),
            concatMap((action) =>
                of({ session: action.session }).pipe(
                    tap(() => window.location.hash = ''),
                    map(({ session }) => {
                        this.#storeSessionInLocalStorage(session);
                        return AuthActions.sessionCreated({ session: session });
                    })
                )
            )
        ));

    sessionCreated$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.sessionCreated),
            // tap(() => this.deskriptorenSearchFacade.load()),
            map(() => {
                // this.router.navigateByUrl('dashboard')
                return noopAction();
            })
        )
    );

    sessionRestored$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.sessionRestored),
            map((action) => {
                if (action.session) {
                    // this.deskriptorenSearchFacade.load();
                }
                return noopAction();
            })
        )
    );

    logout$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.logout),
            this.safeNgrx.safeSwitchMap((action) =>
                this.authHttpService.logout().pipe(
                    tap(() => {
                        this.authHttpService.logout();
                        this.#clearSession();
                        this.messageService.clear();
                    }),
                    map(() => AuthActions.userLoggedOut())
                ), '', noopAction()
            ))
    );

    userLoggedOut$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.userLoggedOut),
            tap(() => {
                this.authHttpService.logout();
                this.#clearSession();
                this.messageService.clear();
                this.router.navigateByUrl('')
            })
        ), { dispatch: false }
    );

    sessionExpired$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.sessionExpired),
            map((action) => {
                this.#checkSessionInLocalStorage();
                this.router.navigateByUrl('');
                this.messageService.message(action.message);
                return noopAction();
            })
        )
    );

    clearOrRestoreSession$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.clearOrRestoreSession),
            switchMap(() => this.#checkSessionInLocalStorage()),
            map((session: Session | undefined) => this.#getClearOrRestoreResultAction(session))
        ));

    #getClearOrRestoreResultAction(session?: Session): Action {

        if (session) {
            return AuthActions.sessionRestored({ session: session });
        }
        return AuthActions.sessionCleared();
    }

    #checkSessionInLocalStorage(): Observable<Session | undefined> {

        const session = localStorage.getItem(this.#storagePrefix + STORAGE_KEY_SESSION);

        if (!session) {
            return of(undefined);
        }
        return of(JSON.parse(session));
    }

    #storeSessionInLocalStorage(session: Session): void {
        localStorage.setItem(this.#storagePrefix + STORAGE_KEY_SESSION, JSON.stringify(session));
    }

    #clearSession(): void {
        this.safeNgrx.clearSession();
        const storageKeySessionState = this.configuration.storagePrefix + 'SESSIONSTATE';
        localStorage.removeItem(storageKeySessionState);
    }
}
