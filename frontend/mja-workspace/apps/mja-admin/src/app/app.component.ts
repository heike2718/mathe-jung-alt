import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { AuthFacade, AuthResult } from '@mja-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'mja-admin';

  constructor(public authFacade: AuthFacade, private breakpointObserver: BreakpointObserver) { }

  ngOnInit(): void {   

    const hash = window.location.hash;

    if (hash && hash.indexOf('idToken') > 0) {
      const authResult = this.authFacade.parseHash(hash) as AuthResult;

      if (authResult.state) {
        if (authResult.state === 'login') {
          this.authFacade.createSession(authResult);
        }
      } else {
        window.location.hash = '';
      }
    } else {
      this.authFacade.clearOrRestoreSession();
    }

  }
}
