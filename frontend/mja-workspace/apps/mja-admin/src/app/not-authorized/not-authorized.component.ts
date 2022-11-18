import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'mja-admin-not-authorized',
  templateUrl: './not-authorized.component.html',
  styleUrls: ['./not-authorized.component.scss'],
})
export class NotAuthorizedComponent {
  
  constructor(private router: Router) { }

  goHome() {
	  this.router.navigateByUrl('/');
  }
}
