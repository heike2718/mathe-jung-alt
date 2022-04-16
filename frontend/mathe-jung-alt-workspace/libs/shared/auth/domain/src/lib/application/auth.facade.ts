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

    constructor(private store: Store<AuthPartialState>) { }


    public requestLoginRedirectUrl(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.requestLoginUrl());
    }

    public clearOrRestoreSession(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.clearOrRestoreSession());
    }

    public logout(): void {
        this.store.dispatch(AuthActions.logout());
    }

    public createFakeSession(): void {
        const authResult: AuthResult = {
            expiresAt: 7961434408
        };
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.store.dispatch(AuthActions.createSession({ authResult: authResult }));
    }
}