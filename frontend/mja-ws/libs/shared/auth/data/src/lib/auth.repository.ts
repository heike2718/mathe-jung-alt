import { inject, Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { isAdmin } from './internal.model';
import { authActions } from './auth.actions';
import { Observable, of, switchMap } from 'rxjs';
import { fromAuth } from './auth.selectors';
import { filterDefined } from '@mja-ws/shared/ngrx-utils';
import { AuthResult, User } from '@mja-ws/shared/auth/model';

@Injectable({
    providedIn: 'root'
})
export class AuthRepository {

    #store = inject(Store);

    readonly user$: Observable<User> = this.#store.select(fromAuth.user).pipe(
        filterDefined,
        switchMap((user) => of({ fullName: user.fullName, isAdmin: isAdmin(user), anonym: user.anonym }))
    );

    readonly loggedIn$: Observable<boolean> = this.#store.select(fromAuth.user).pipe(
        switchMap((user) => of(!user.anonym))
    );

    public login(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.#store.dispatch(authActions.request_login_url());
    }

    public logout(): void {
        this.#store.dispatch(authActions.log_out());
    }

    public handleSessionExpired(): void {
        this.#store.dispatch(authActions.logged_out());
    }

    public createSession(authResult: AuthResult): void {
        this.#store.dispatch(authActions.init_session({ authResult }));
    }


}