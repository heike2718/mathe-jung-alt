import { Component, EventEmitter, inject, Output } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterLinkWithHref } from '@angular/router';
import { AuthFacade } from '@mja-ws/shared/auth/api';

@Component({
  selector: 'mja-app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
  standalone: true,
  imports: [MatListModule, MatIconModule, NgIf, AsyncPipe, RouterLinkWithHref],
})
export class SidenavComponent {

  public authFacade = inject(AuthFacade);
  
  @Output()
  sidenavClose = new EventEmitter();

  public login(): void {
    this.authFacade.login();
  }

  public signup(): void {
    this.authFacade.signup();
  }

  public logout(): void {
    this.authFacade.logout();
  }
  
  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }
}
