import { inject, Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { isAdmin } from './internal.model';
import { authActions } from './auth.actions';
import { Observable, of, switchMap } from 'rxjs';
import { fromAuth } from './auth.selectors';
import { filterDefined } from '@mja-ws/shared/ngrx-utils';
import { AuthResult, BENUTZERART, User } from '@mja-ws/shared/auth/model';

@Injectable({
    providedIn: 'root'
})
export class AuthRepository {

    #store = inject(Store);

    readonly benutzerart$: Observable<BENUTZERART> = this.#store.select(fromAuth.benutzerart);   

    readonly user$: Observable<User> = this.#store.select(fromAuth.user).pipe(
        filterDefined,
        switchMap((user) => of({ fullName: user.fullName, isAdmin: isAdmin(user), anonym: user.anonym, benutzerart: user.benutzerart }))
    );
    
    readonly loggedIn$: Observable<boolean> = this.#store.select(fromAuth.user).pipe(
        switchMap((user) => of(!user.anonym))
    );

    public login(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.#store.dispatch(authActions.rEQUEST_LOGIN_URL());
    }

    public signUp(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.#store.dispatch(authActions.rEQUEST_SIGNUP_URL());
    }

    public logout(): void {
        this.#store.dispatch(authActions.lOG_OUT());
    }

    public handleSessionExpired(): void {
        this.#store.dispatch(authActions.lOGGED_OUT());
    }

    public createSession(authResult: AuthResult): void {
        this.#store.dispatch(authActions.iNIT_SESSION({ authResult }));
    }


}