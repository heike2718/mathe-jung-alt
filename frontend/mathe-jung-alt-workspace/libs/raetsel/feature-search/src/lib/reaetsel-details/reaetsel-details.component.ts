import { Component, Input, OnInit } from '@angular/core';
import { RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './reaetsel-details.component.html',
  styleUrls: ['./reaetsel-details.component.scss']
})
export class ReaetselDetailsComponent implements OnInit {

  panelOpenState = false;

  constructor(public raetselFacade: RaetselFacade, public authFacade: AuthFacade) { }

  ngOnInit(): void {
  }

}
