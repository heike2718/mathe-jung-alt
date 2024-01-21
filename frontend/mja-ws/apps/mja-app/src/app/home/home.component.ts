import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthFacade } from '@mja-ws/core/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Configuration } from '@mja-ws/shared/config';
import { MatButtonModule } from '@angular/material/button';
import { Subscription } from 'rxjs';
import { User } from '@mja-ws/core/model';

@Component({
  selector: 'mja-app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule, MatButtonModule]
})
export class HomeComponent implements OnInit, OnDestroy {

  authFacade = inject(AuthFacade);

  coreFacade = inject(CoreFacade);

  user!: User;

  #configService = inject(Configuration);

  version = "2.6.9";

  imageSourceLogo = '';

  #userSubscription = new Subscription();

  ngOnInit(): void {

    this.imageSourceLogo = this.#configService.assetsPath + 'mja_logo_2.svg';
    this.coreFacade.loadAnzahlPublicRaetsel(); 
    
    this.#userSubscription = this.authFacade.user$.subscribe((user) => this.user = user);
  }

  ngOnDestroy(): void {
      this.#userSubscription.unsubscribe();
  }

  login(): void {
    this.authFacade.login();
  }

  signup(): void {
    this.authFacade.signup();
  }
}
