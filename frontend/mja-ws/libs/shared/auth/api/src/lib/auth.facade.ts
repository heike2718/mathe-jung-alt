import { inject, Injectable } from '@angular/core';
import { AuthRepository } from '@mja-ws/shared/auth/data';
import { AuthResult, User } from '@mja-ws/shared/auth/model';
import { Observable, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthFacade {

  #authRepository = inject(AuthRepository);

  readonly user$: Observable<User> = this.#authRepository.user$;
  readonly userIsLoggedIn$: Observable<boolean> = this.#authRepository.loggedIn$;
  readonly userIsLoggedOut$: Observable<boolean> = this.#authRepository.loggedIn$.pipe(
    switchMap((li) => of(!li))
  );

  public login(): void {
    this.#authRepository.login();
  }

  public initClearOrRestoreSession(): void {

    const hash = window.location.hash;

    if (hash && hash.indexOf('idToken') > 0) {

      const authResult: AuthResult = this.#parseHash(hash);

      if (authResult.state) {
        if (authResult.state === 'login') {
          this.#authRepository.createSession(authResult);
        } else {
          window.location.hash = '';
        }
      }

    } else {
      console.log('#authRepository.clearOrRestoreSession()');
    }

  }

  public logout(): void {
    this.#authRepository.logout();
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
    return result;
  }
}