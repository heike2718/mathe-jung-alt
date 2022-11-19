import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { LayoutComponent } from './layout/layout.component';
import { HeaderComponent } from './navigation/header/header.component';
import { SidenavComponent } from './navigation/sidenav/sidenav.component';

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
    SidenavComponent
  ]
})
export class AppComponent {
  title = 'mja-admin';
}
