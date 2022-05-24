import { Component, Input, OnInit } from '@angular/core';
import { RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './reaetsel-details.component.html',
  styleUrls: ['./reaetsel-details.component.scss']
})
export class ReaetselDetailsComponent implements OnInit {

  panelOpenState = false;

  constructor(public raetselFacade: RaetselFacade) { }

  ngOnInit(): void {
  }

}