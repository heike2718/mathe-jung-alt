import { Component, OnInit } from '@angular/core';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  
  constructor(public quellenFacade: QuellenFacade, public authFacade: AuthFacade) {}

  ngOnInit(): void {}
}

