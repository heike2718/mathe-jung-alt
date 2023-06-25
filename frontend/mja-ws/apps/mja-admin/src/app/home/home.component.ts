import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { CoreFacade } from '@mja-ws/core/api';
import { Configuration } from '@mja-ws/shared/config';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class HomeComponent implements OnInit {

  authFacade = inject(AuthFacade);

  coreFacade = inject(CoreFacade);

  #configService = inject(Configuration);

  version = "1.5.1";

  imageSourceLogo = '';

  ngOnInit(): void {

    this.imageSourceLogo = this.#configService.assetsPath + 'mja_logo_2.svg';
      
  }
}
