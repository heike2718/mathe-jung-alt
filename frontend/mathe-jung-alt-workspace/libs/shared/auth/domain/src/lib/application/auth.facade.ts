import { Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import * as AuthActions from '../+state/auth.actions';
import { AuthPartialState } from "../+state/auth.reducer";
import { AuthResult } from "../entities/auth.model";


@Injectable({
    providedIn: 'root'
})
export class AuthFacade {

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