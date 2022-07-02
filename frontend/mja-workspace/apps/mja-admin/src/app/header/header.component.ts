import { BreakpointObserver } from '@angular/cdk/layout';
import { AfterViewInit, Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { environment } from '../../environments/environment';

@Component({
  selector: 'mja-admin-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit, AfterViewInit {

  titel = 'Mja Admin (' + environment.version + ')';

  largeDevice = true;

  constructor(public authFacade: AuthFacade, private breakpointObserver: BreakpointObserver) { }

  ngOnInit(): void { }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.breakpointObserver.observe(['(max-width: 800px)']).subscribe((res) => {
        if (res.matches) {
         this.largeDevice = false;
        }
      });
    }, 1);
  }

  login(): void {
    this.authFacade.requestLoginRedirectUrl();
  }

  logout(): void {
    this.authFacade.logout();
  }
}
