import { CommonModule } from '@angular/common';
import { Component, NgModule, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'mja-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {

  titel = 'Mja Admin (' + environment.version + ')';

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }

  public login(): void {

    if (!environment.production && environment.withFakeLogin) {

      this.authFacade.createFakeSession();

    } else {

      this.authFacade.requestLoginRedirectUrl();

    }

  }

  public logout(): void {
    this.authFacade.logout();
  }
}

@NgModule({
  imports: [CommonModule, MaterialModule, RouterModule],
  declarations: [HeaderComponent],
  exports: [HeaderComponent],
})
export class HeaderComponentModule { }

