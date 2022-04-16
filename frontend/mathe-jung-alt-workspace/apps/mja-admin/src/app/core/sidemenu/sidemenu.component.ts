import { CommonModule } from '@angular/common';
import { Component, NgModule, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';

@Component({
  selector: 'mja-admin-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss'],
})
export class SidemenuComponent implements OnInit {

  constructor() { }

  ngOnInit(): void { }
}


@NgModule({
  imports: [CommonModule, MaterialModule, RouterModule],
  declarations: [SidemenuComponent],
  exports: [SidemenuComponent],
})
export class SidemenuComponentModule { }
