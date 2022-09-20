import { Component, OnInit } from '@angular/core';
import { RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';

@Component({
  selector: 'mja-raetselgruppe-edit',
  templateUrl: './raetselgruppe-edit.component.html',
  styleUrls: ['./raetselgruppe-edit.component.scss'],
})
export class RaetselgruppeEditComponent implements OnInit {
  constructor(private raetselgruppenFacade: RaetselgruppenFacade) {}

  ngOnInit() {}
}
