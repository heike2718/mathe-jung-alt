import { BreakpointObserver } from '@angular/cdk/layout';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthFacade, AuthResult } from '@mja-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'mja-admin';

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  constructor(public authFacade: AuthFacade, private breakpointObserver: BreakpointObserver) { }

  ngOnInit(): void {

    this.authFacade.clearOrRestoreSession();

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
    }

  }

  ngAfterViewInit() {

    setTimeout(() => {
      this.breakpointObserver.observe(['(max-width: 800px)']).subscribe((res) => {
        if (res.matches) {
          this.sidenav.mode = 'over';
          this.sidenav.close();
        } else {
          this.sidenav.mode = 'side';
          this.sidenav.open();
        }
      });
    }, 1);
  }
}
