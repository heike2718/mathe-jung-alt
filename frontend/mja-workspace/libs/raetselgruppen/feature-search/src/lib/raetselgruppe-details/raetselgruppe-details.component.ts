import { Component, OnDestroy, OnInit } from '@angular/core';
import { RaetselgruppeDetails, RaetselgruppenFacade, RaetselgruppensucheTrefferItem } from '@mja-workspace/raetselgruppen/domain';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-raetselgruppe',
  templateUrl: './raetselgruppe-details.component.html',
  styleUrls: ['./raetselgruppe-details.component.scss'],
})
export class RaetselgruppeDetailsComponent implements OnInit {

  constructor(public raetselgruppenFacade: RaetselgruppenFacade) { }

  ngOnInit(): void {    
  }
}
