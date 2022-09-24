import { Component, OnInit } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { environment } from '../../environments/environment';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  #storageKeySessionState = environment.storageKeyPrefix + 'SESSIONSTATE';

  version = environment.version;

  menuOpen = false;

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void {
    this.#checkSessionState();
  }

  onMenuStateChanged($event: any): void {
    this.menuOpen = $event;
  }

  public login(): void {
    this.authFacade.login();
  }

  public logout(): void {
    this.authFacade.logout();
  }

  #checkSessionState(): void {
    // ist ein bisschen von hinten durch die Brust ins Auge, weil ich nicht pollen will 
    // und die 440 vom Server im SafeNgrxService aufschlägt, der keine Referenz auf AuthFacade haben kann wegen circular dependency.
    const sessionState = localStorage.getItem(this.#storageKeySessionState);
    if ('expired' === sessionState) {
      this.authFacade.logout();
    }
  }
}
