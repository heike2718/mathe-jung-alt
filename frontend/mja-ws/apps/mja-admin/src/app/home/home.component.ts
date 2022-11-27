import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthFacade } from '@mja-ws/shared/auth/api';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class HomeComponent {

  authFacade = inject(AuthFacade);
  version = "1.4.0";
}
