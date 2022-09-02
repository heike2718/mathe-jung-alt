import { Component, OnInit } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { environment } from '../../environments/environment';

@Component({
  selector: 'mja-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  
  titel = 'Mja Admin (' + environment.version + ')';

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }

  public login(): void {
    this.authFacade.login();
  }

  public logout(): void {
    this.authFacade.logout();
  }
  
}
