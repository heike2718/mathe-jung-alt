import { AsyncPipe, NgIf } from '@angular/common';
import { Component, EventEmitter, inject, OnDestroy, OnInit, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLinkWithHref } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthFacade } from '@mja-ws/core/api';
import { Subscription } from 'rxjs';
import { User } from '@mja-ws/core/model';

@Component({
  selector: 'mja-app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatToolbarModule, NgIf, AsyncPipe, RouterLinkWithHref]
})
export class HeaderComponent implements OnInit, OnDestroy{

  @Output()
  sidenavToggle = new EventEmitter();

  #breakpointObserver = inject(BreakpointObserver);
  
  authFacade = inject(AuthFacade);

  user!: User;

  #userSubscription = new Subscription;

  ngOnInit(): void {
      this.#userSubscription = this.authFacade.user$.subscribe((user) => this.user = user);
  }

  ngOnDestroy(): void {
      this.#userSubscription.unsubscribe();
  }

  get isHandset(): boolean {
    return this.#breakpointObserver.isMatched(Breakpoints.Handset);
  }

  login(): void {
    this.authFacade.login();
  }

  signup(): void {
    this.authFacade.signup();
  }

  logout(): void {
    this.authFacade.logout();
  }

  onToggleSidenav(): void {
    this.sidenavToggle.emit();
  }
}
