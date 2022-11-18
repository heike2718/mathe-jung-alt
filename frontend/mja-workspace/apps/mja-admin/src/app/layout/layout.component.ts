
import { Component, DoCheck, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';


@Component({
  selector: 'mja-admin-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
})
export class LayoutComponent implements OnInit, DoCheck {

  isMenuOpen = true;

  constructor(public authFacade: AuthFacade, private breakpointObserver: BreakpointObserver) { }

  get isHandset(): boolean {
    return this.breakpointObserver.isMatched(Breakpoints.Handset);
  }

  ngOnInit() {
    this.isMenuOpen = true;  // Open side menu by default
  }

  ngDoCheck() {
    if (this.isHandset) {
      this.isMenuOpen = false;
    } else {
      this.isMenuOpen = true;
    }
  }

  login(): void {
    this.authFacade.login();
  }

  logout(): void {
    this.authFacade.logout();    
  }

}
