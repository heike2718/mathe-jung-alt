import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthFacade } from '@rbk-ws/core/api';
import { CoreFacade } from '@rbk-ws/core/api';
import { Configuration } from '@rbk-ws/shared/config';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';
import { User } from '@rbk-ws/core/model';
import { Router } from '@angular/router';

@Component({
  selector: 'rbk-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, MatButtonModule, MatCardModule, MatIconModule],
})
export class HomeComponent implements OnInit, OnDestroy {
  authFacade = inject(AuthFacade);

  #coreFacade = inject(CoreFacade);

  user!: User;

  #configService = inject(Configuration);
  #router = inject(Router);

  version = '4.0.1';
  anzahlRaetsel = 0;

  imageSourceLogo = '';

  #subscriptions = new Subscription();

  ngOnInit(): void {
    this.imageSourceLogo = this.#configService.assetsPath + 'mja_logo_2-gruen.svg';
    this.#coreFacade.loadAnzahlPublicRaetsel();

    const userSubscription = this.authFacade.user$.subscribe(user => (this.user = user));
    this.#subscriptions.add(userSubscription);

    const anzahlRaetselSubscription = this.#coreFacade.anzahlPublicRaetsel$.subscribe(
      anzahl => (this.anzahlRaetsel = anzahl)
    );
    this.#subscriptions.add(anzahlRaetselSubscription);
  }

  ngOnDestroy(): void {
    this.#subscriptions.unsubscribe();
  }

  login(): void {
    this.authFacade.login();
  }

  signup(): void {
    this.authFacade.signup();
  }

  navigateTo(route: string) {
    this.#router.navigate([route]);
  }
}
