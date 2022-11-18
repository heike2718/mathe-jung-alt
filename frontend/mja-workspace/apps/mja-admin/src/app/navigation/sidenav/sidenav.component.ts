import { Component, EventEmitter, Output } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
})
export class SidenavComponent {


  @Output()
  sidenavClose = new EventEmitter();

  constructor(public authFacade: AuthFacade) { }
  
  public login(): void {
    this.authFacade.login();
  }

  public logout(): void {
    this.authFacade.logout();
  }
  
  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }


}
