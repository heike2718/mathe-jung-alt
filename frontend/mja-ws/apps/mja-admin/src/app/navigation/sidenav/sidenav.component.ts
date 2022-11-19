import { Component, EventEmitter, Output } from '@angular/core';
import { NgIf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterLinkWithHref } from '@angular/router';

@Component({
  selector: 'mja-admin-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
  standalone: true,
  imports: [MatListModule, MatIconModule, NgIf, RouterLinkWithHref],
})
export class SidenavComponent {
  
  @Output()
  sidenavClose = new EventEmitter();

  public login(): void {
    // this.authFacade.login();
  }

  public logout(): void {
    // this.authFacade.logout();
  }
  
  public onSidenavClose = () => {
    this.sidenavClose.emit();
  }
}
