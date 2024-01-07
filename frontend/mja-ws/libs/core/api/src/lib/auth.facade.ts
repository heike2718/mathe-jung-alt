import { inject, Injectable } from '@angular/core';
import { authActions, fromAuth } from '@mja-ws/core/data';
import { AuthResult, Benutzerart, isAdmin, User } from '@mja-ws/core/model';
import { MessageService } from '@mja-ws/shared/messaging/api';
import { filterDefined } from '@mja-ws/shared/util';
import { Store } from '@ngrx/store';
import { Observable, of, switchMap, tap } from 'rxjs';
import { CoreFacade } from './core.facade';

@Injectable({
  providedIn: 'root'
})
export class AuthFacade {

  #store = inject(Store);
  #coreFacade = inject(CoreFacade);

  constructor() {

    this.#store.select(fromAuth.session).pipe(
      tap((session) => {
        if (!session.user.anonym) {

          const benutzerart: Benutzerart = session.user.benutzerart;

          if (benutzerart === 'ADMIN' || benutzerart === 'AUTOR') {
            this.#coreFacade.loadAutor();
          }
          this.#coreFacade.loadDeskriptoren();
        }
      })
    ).subscribe();
  }

  // #authRepository = inject(AuthRepository);
  #messageService = inject(MessageService);

  readonly userIsRoot$: Observable<boolean> = this.#store.select(fromAuth.userIsRoot);

  readonly userIsAdmin$: Observable<boolean> = this.#store.select(fromAuth.userIsAdmin);

  readonly userIsPublic$: Observable<boolean> = this.#store.select(fromAuth.userIsPublic);

  readonly user$: Observable<User> = this.#store.select(fromAuth.userFull).pipe(
    filterDefined,
    switchMap((user) => of({ fullName: user.fullName, isAdmin: isAdmin(user), anonym: user.anonym, benutzerart: user.benutzerart })));

  readonly userIsLoggedIn$: Observable<boolean> = this.#store.select(fromAuth.userFull).pipe(
    switchMap((user) => of(!user.anonym))
  );

  readonly userIsLoggedOut$: Observable<boolean> = this.userIsLoggedIn$.pipe(
    switchMap((li) => of(!li))
  );

  login(): void {
    // Dies triggert einen SideEffect (siehe auth.effects.ts)
    this.#store.dispatch(authActions.rEQUEST_LOGIN_URL());
  }

  signup(): void {
    // Dies triggert einen SideEffect (siehe auth.effects.ts)
    this.#store.dispatch(authActions.rEQUEST_SIGNUP_URL());
  }

  initClearOrRestoreSession(): void {

    const hash = window.location.hash;

    if (hash && hash.indexOf('idToken') > 0) {

      const authResult: AuthResult = this.#parseHash(hash);

      if (authResult.state) {
        if (authResult.state === 'login') {
          this.#store.dispatch(authActions.iNIT_SESSION({ authResult }));          
        }
        if (authResult.state === 'signup') {
          window.location.hash = '';
          this.#messageService.info('Ihr Benutzerkonto wurde angelegt und muss noch aktiviert werden. Bitte schauen Sie in Ihrem Postfach nach.')
        }
      } else {
        window.location.hash = '';
      }
    }
  }

  logout(): void {
    this.#store.dispatch(authActions.lOG_OUT());
    this.#coreFacade.handleLogout();
  }

  handleSessionExpired(): void {
    this.#store.dispatch(authActions.lOGGED_OUT());
    this.#messageService.warn('Die Session ist abgelaufen. Bitte erneut einloggen.');
  }

  #parseHash(hash: string): AuthResult {

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
    window.location.hash = '';
    return result;
  }  
};