import { AsyncPipe, NgIf } from '@angular/common';
import { Component, EventEmitter, inject, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLinkWithHref } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthFacade } from '@mja-ws/shared/auth/api';

@Component({
  selector: 'mja-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatToolbarModule, NgIf, AsyncPipe, RouterLinkWithHref]
})
export class HeaderComponent {

  @Output()
  sidenavToggle = new EventEmitter();

  #breakpointObserver = inject(BreakpointObserver);
  
  public authFacade = inject(AuthFacade);

  get isHandset(): boolean {
    return this.#breakpointObserver.isMatched(Breakpoints.Handset);
  }

  login(): void {
    this.authFacade.login();
  }

  logout(): void {
    this.authFacade.logout();
  }

  onToggleSidenav(): void {
    this.sidenavToggle.emit();
  }
}
