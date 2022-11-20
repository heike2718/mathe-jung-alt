import { inject, Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserFull } from './internal.model';
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
        switchMap((user) => of({ fullName: user.fullName, isAdmin: this.#isAdmin(user) }))
    );

    readonly loggedIn$: Observable<boolean> = this.#store.select(fromAuth.user).pipe(
        switchMap((user) => of(!user.anonym))
    );

    public login(): void {
        // Dies triggert einen SideEffect (siehe auth.effects.ts)
        this.#store.dispatch(authActions.requestLoginUrl());
    }    

    public logout(): void {
        this.#store.dispatch(authActions.logout());
    }

    public createSession(authResult: AuthResult): void {
        this.#store.dispatch(authActions.initSession({ authResult }));
    }

    #isAdmin(user: UserFull): boolean {
        return user.roles.indexOf('ADMIN') + user.roles.indexOf('AUTOR') >= 0;
    }
}