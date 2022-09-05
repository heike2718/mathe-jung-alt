import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'mja-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  
  titel = 'Mja Admin (' + environment.version + ')';

  @Output() 
  sidenavToggle = new EventEmitter();

  constructor(public authFacade: AuthFacade, private breakpointObserver: BreakpointObserver) { }

  get isHandset(): boolean {
    return this.breakpointObserver.isMatched(Breakpoints.Handset);
  }

  ngOnInit(): void { }

  public login(): void {
    this.authFacade.login();
  }

  public logout(): void {
    this.authFacade.logout();
  }

  public onToggleSidenav = () => {
    this.sidenavToggle.emit();
  }  
}
