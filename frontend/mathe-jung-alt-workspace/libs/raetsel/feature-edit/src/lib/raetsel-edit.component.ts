import { Component, OnInit } from '@angular/core';
import { RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';

@Component({
  selector: 'raetsel-raetsel-feature-edit',
  templateUrl: './raetsel-edit.component.html',
  styleUrls: ['./raetsel-edit.component.scss'],
})
export class RaetselEditComponent implements OnInit {

  constructor(public raetselFacade: RaetselFacade) { }

  ngOnInit() {}
}
