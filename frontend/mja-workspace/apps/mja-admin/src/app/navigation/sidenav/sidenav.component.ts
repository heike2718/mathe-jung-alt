import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
})
export class SidenavComponent implements OnInit {


  @Output()
  sidenavClose = new EventEmitter();

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }

  
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
