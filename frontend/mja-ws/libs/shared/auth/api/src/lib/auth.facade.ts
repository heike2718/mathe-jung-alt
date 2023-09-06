import { inject, Injectable } from '@angular/core';
import { CoreFacade } from '@mja-ws/core/api';
import { AuthRepository } from '@mja-ws/shared/auth/data';
import { AuthResult, BENUTZERART, User } from '@mja-ws/shared/auth/model';
import { MessageService } from '@mja-ws/shared/messaging/api';
import { Observable, of, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthFacade {

  #authRepository = inject(AuthRepository);
  #coreFacade = inject(CoreFacade);
  #messageService = inject(MessageService);

  readonly userIsAdmin$: Observable<boolean> = this.#authRepository.benutzerart$.pipe(
    switchMap((benutzerart) => of(benutzerart === 'ADMIN' || benutzerart === 'AUTOR'))
  );

  readonly userIsPublic$: Observable<boolean> = this.#authRepository.benutzerart$.pipe(
    switchMap((benutzerart) => of(benutzerart === 'STANDARD'))
  );

  readonly user$: Observable<User> = this.#authRepository.user$;

  readonly userIsLoggedIn$: Observable<boolean> = this.#authRepository.loggedIn$;

  readonly userIsLoggedOut$: Observable<boolean> = this.#authRepository.loggedIn$.pipe(
    switchMap((li) => of(!li))
  );

  public login(): void {
    this.#authRepository.login();
  }

  public signup(): void {
    this.#authRepository.signUp();
  }

  public initClearOrRestoreSession(): void {

    const hash = window.location.hash;

    if (hash && hash.indexOf('idToken') > 0) {

      const authResult: AuthResult = this.#parseHash(hash);

      if (authResult.state) {
        if (authResult.state === 'login') {
          this.#authRepository.createSession(authResult);
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

  public logout(): void {
    this.#authRepository.logout();
  }

  public handleSessionExpired(): void {
    this.#authRepository.handleSessionExpired();
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