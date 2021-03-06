
import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Configuration, SharedConfigService } from '@mja-workspace/shared/util-configuration';
import { MessageService, SafeNgrxService, noopAction, isExpired } from '@mja-workspace/shared/util-mja';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Action, Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { concatMap, map, switchMap, tap } from 'rxjs/operators';
import { Session, STORAGE_KEY_DEV_SESSION_ID, STORAGE_KEY_SESSION_EXPIRES_AT, STORAGE_KEY_USER, User } from '../entities/auth.model';
import { AuthHttpService } from '../infrastructure/auth-http.service';
import * as AuthActions from './auth.actions';


@Injectable()
export class AuthEffects {

    #storagePrefix!: string;


    constructor(private actions$: Actions,
        @Inject(SharedConfigService) private configuration: Configuration,
        private globalStore: Store,
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
                    tap(() => console.log(JSON.stringify(action.authResult))),
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
                        this.storeSessionInLocalStorage(session);
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
            map(() => {
                this.clearSession();
                this.router.navigateByUrl('');
                return AuthActions.userLoggedOut();
            })
        )
    );

    clearOrRestoreSession$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.clearOrRestoreSession),
            switchMap(() => this.checkSessionInLocalStorage()),
            map((session: Session | undefined) => this.getClearOrRestoreResultAction(session))
        ));

    private getClearOrRestoreResultAction(session?: Session): Action {

        if (session) {
            return AuthActions.sessionRestored({ session: session });
        }
        return AuthActions.sessionCleared();
    }

    private checkSessionInLocalStorage(): Observable<Session | undefined> {

        const u = localStorage.getItem(this.#storagePrefix + STORAGE_KEY_USER);

        if (!u) {
            return of(undefined);
        }

        const expiresAt = localStorage.getItem(this.#storagePrefix + STORAGE_KEY_SESSION_EXPIRES_AT);

        // TODO: wenn abgelaufen
        if (!expiresAt || isExpired(JSON.parse(expiresAt))) {
            return of(undefined);
        }

        const exp: number = JSON.parse(expiresAt);
        const user: User = JSON.parse(u);

        return of({
            expiresAt: exp,
            user: user
        });
    }

    private storeSessionInLocalStorage(session: Session): void {

        const user = session.user;
        localStorage.setItem(this.#storagePrefix + STORAGE_KEY_USER, JSON.stringify(user));
        if (session.sessionId) {
            localStorage.setItem(this.#storagePrefix + STORAGE_KEY_DEV_SESSION_ID, session.sessionId);
        }
        localStorage.setItem(this.#storagePrefix + STORAGE_KEY_SESSION_EXPIRES_AT, JSON.stringify(session.expiresAt));
    }

    private clearSession(): void {
        localStorage.removeItem(this.#storagePrefix + STORAGE_KEY_DEV_SESSION_ID);
        localStorage.removeItem(this.#storagePrefix + STORAGE_KEY_SESSION_EXPIRES_AT);
        localStorage.removeItem(this.#storagePrefix + STORAGE_KEY_USER);
        this.messageService.clear();
        this.globalStore.dispatch(AuthActions.clearStoreAction())
        // this.suchfilterFacade.clearAll();
    }
}
