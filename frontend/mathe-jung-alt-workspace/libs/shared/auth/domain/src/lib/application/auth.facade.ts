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
        this.store.dispatch(AuthActions.requestLoginUrl());
    }



    public createFakeSession(): void {
        const authResult: AuthResult = {
            expiresAt: 7961434408
        };
        this.store.dispatch(AuthActions.createSession({ authResult: authResult }));
    }

}