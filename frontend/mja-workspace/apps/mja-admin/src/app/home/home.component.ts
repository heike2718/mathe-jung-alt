import { Component, OnInit } from '@angular/core';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  
  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }
}
