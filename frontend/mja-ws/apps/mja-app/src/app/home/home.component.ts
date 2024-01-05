import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthFacade } from '@mja-ws/core/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Configuration } from '@mja-ws/shared/config';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'mja-app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule, MatButtonModule]
})
export class HomeComponent implements OnInit {

  authFacade = inject(AuthFacade);

  coreFacade = inject(CoreFacade);

  #configService = inject(Configuration);

  version = "2.5.0";

  imageSourceLogo = '';

  ngOnInit(): void {

    this.imageSourceLogo = this.#configService.assetsPath + 'mja_logo_2.svg';
    this.coreFacade.loadAnzahlPublicRaetsel();      
  }

  login(): void {
    this.authFacade.login();
  }

  signup(): void {
    this.authFacade.signup();
  }
}
