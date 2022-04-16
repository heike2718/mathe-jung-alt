import { CommonModule } from '@angular/common';
import { Component, NgModule, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';

@Component({
  selector: 'mja-admin-not-authorized',
  templateUrl: './not-authorized.component.html',
  styleUrls: ['./not-authorized.component.scss']
})
export class NotAuthorizedComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  goHome() {
	  this.router.navigateByUrl('/');
  }

}


@NgModule({
  imports: [CommonModule, MaterialModule],
  declarations: [NotAuthorizedComponent],
  exports: [NotAuthorizedComponent],
})
export class NotAuthorizedComponentModule { }
