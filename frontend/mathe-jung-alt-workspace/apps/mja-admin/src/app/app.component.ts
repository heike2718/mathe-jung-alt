import { Component, OnInit } from '@angular/core';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  title = 'mja-admin';

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void {

    this.authFacade.clearOrRestoreSession();

    // TODO: hier schauen, ob ein url.hash da ist und dann Zeug zum Beenden des logins tun
  }
}
