import { CommonModule } from '@angular/common';
import { Component, NgModule, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';

@Component({
  selector: 'mja-admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}

