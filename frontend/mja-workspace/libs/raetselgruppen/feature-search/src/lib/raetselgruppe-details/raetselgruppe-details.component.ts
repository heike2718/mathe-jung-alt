import { Component, OnDestroy, OnInit } from '@angular/core';
import { RaetselgruppenFacade, RaetselgruppensucheTrefferItem } from '@mja-workspace/raetselgruppen/domain';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-raetselgruppe',
  templateUrl: './raetselgruppe-details.component.html',
  styleUrls: ['./raetselgruppe-details.component.scss'],
})
export class RaetselgruppeDetailsComponent implements OnInit, OnDestroy {

  #selectedGruppeSubscription = new Subscription();

  #selectedGruppe?: RaetselgruppensucheTrefferItem;


  constructor(public raetsegruppenFacade: RaetselgruppenFacade) {}

  ngOnInit(): void {

    this.#selectedGruppeSubscription = this.raetsegruppenFacade.selectedGruppe$.pipe(
      tap((gruppe) => {
        if (gruppe) {
          this.#selectedGruppe = gruppe;
        }
      })
    ).subscribe();

  }

  ngOnDestroy(): void {
      this.#selectedGruppeSubscription.unsubscribe();
  }
}
