import { Injectable } from '@angular/core';
import { PageDefinition, Suchfilter } from '@mja-workspace/suchfilter/domain';
import { select, Store } from '@ngrx/store';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { Raetsel } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselSearchFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  paginationState$ = this.store.pipe(select(RaetselSelectors.getPaginationState));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));

  constructor(private store: Store<fromRaetsel.RaetselPartialState>) {}

  startSearch(anzahlTreffer: number, suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.raetselCounted({ anzahl: anzahlTreffer }));
    this.#findRaetsel(suchfilter, pageDefinition);
  }

  /*  Setzt die Suchkette mit serverseitiger Pagination in Gang. Hierzu wird ein select count mit dem Suchfilter abgesetzt */
  triggerSearch(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.selectPage({ pageDefinition }));
    this.store.dispatch(RaetselActions.prepareSearch({suchfilter, pageDefinition}));
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({ raetsel }));
  }

// ///// private methods //////

  #findRaetsel(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.findRaetsel({suchfilter, pageDefinition, kontext: 'RAETSEL'}));
  }  
}
