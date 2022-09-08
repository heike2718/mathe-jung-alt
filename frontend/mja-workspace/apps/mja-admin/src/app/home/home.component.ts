import { Component, OnInit } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { environment } from '../../environments/environment';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  version = environment.version;

  menuOpen = false;

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }

  onMenuStateChanged($event: any): void {

    this.menuOpen = $event;

  }

  public login(): void {
    this.authFacade.login();
  }

  public logout(): void {
    this.authFacade.logout();
  }
}
