import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { LayoutComponent } from './layout/layout.component';
import { HeaderComponent } from './navigation/header/header.component';
import { SidenavComponent } from './navigation/sidenav/sidenav.component';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { LoadingIndicatorComponent, MessageComponent } from '@mja-ws/shared/messaging/ui';


@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true,
  imports: [
    RouterOutlet,
    MatToolbarModule,
    MatSidenavModule,
    LayoutComponent,
    HeaderComponent,
    SidenavComponent,
    MessageComponent,
    LoadingIndicatorComponent
  ]
})
export class AppComponent implements OnInit {

  title = 'mja-admin';

  #authFacade = inject(AuthFacade);

  ngOnInit(): void {
    this.#authFacade.initClearOrRestoreSession();
  }
}
