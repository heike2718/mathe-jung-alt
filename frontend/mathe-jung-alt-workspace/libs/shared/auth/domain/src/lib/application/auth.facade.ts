import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import * as AuthActions from '../+state/auth.actions';
import * as AuthSelectors from '../+state/auth.selectors';
import { AuthPartialState } from "../+state/auth.reducer";

import { AuthResult, User } from "../entities/auth.model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class AuthFacade {

    public isLoggedIn$ = this.store.pipe(select(AuthSelectors.isLoggedIn));
    public isLoggedOut$ = this.store.pipe(select(AuthSelectors.isLoggedOut));
    public getUser$: Observable<User | undefined> = this.store.pipe(select(AuthSelectors.getUser));
    public isAuthorized$ = this.store.pipe(select(AuthSelectors.isAuthorized));
    public isSessionExpired$ = this.store.pipe(select(AuthSelectors.isSessionExpired));
    public isAdmin$ = this.store.pipe(select(AuthSelectors.isAdmin));
    public isOrdinaryUser$ = this.store.pipe(select(AuthSelectors.isOrdinaryUser));

    constructor(private store: Store<AuthPartialState>) { }


    public requestLoginRedirectUrl(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.requestLoginUrl());
    }

    public parseHash(hash: string): AuthResult {

        hash = hash.replace(/^#?\/?/, '');

		const result: AuthResult = {
			expiresAt: 0,
			nonce: undefined,
			state: undefined,
			idToken: undefined
		};

		if (hash.length > 0) {

			const tokens = hash.split('&');
			tokens.forEach(
				(token) => {
					const keyVal = token.split('=');
					switch (keyVal[0]) {
						case 'expiresAt': result.expiresAt = JSON.parse(keyVal[1]); break;
						case 'nonce': result.nonce = keyVal[1]; break;
						case 'state': result.state = keyVal[1]; break;
						case 'idToken': result.idToken = keyVal[1]; break;
					}
				}
			);
		}
		return result;
    }

    public clearOrRestoreSession(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.clearOrRestoreSession());
    }

    public logout(): void {
        this.store.dispatch(AuthActions.logout());
    }

    public createSession(authResult: AuthResult): void {
        this.store.dispatch(AuthActions.createSession({authResult}));
    }

    public createFakeSession(): void {
        const authResult: AuthResult = {
            expiresAt: 7961434408
        };
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.createSession({ authResult: authResult }));
    }
}