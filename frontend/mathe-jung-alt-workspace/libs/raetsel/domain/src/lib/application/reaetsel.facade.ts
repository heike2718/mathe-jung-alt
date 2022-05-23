import { Injectable } from '@angular/core';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { select, Store } from '@ngrx/store';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { Raetsel } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  raetselList$ = this.store.pipe(select(RaetselSelectors.getAllRaetsel));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));

  constructor(private store: Store<fromRaetsel.RaetselPartialState>) { }

  findRaetsel(suchfilter: Suchfilter): void {
    this.store.dispatch(RaetselActions.findRaetsel({ suchfilter }));
  }

  slicePage(sortDirection = 'asc', pageIndex = 0, pageSize = 10): void {
    this.store.dispatch(RaetselActions.selectPage({ sortDirection, pageIndex, pageSize }))
  }

  clearTrefferliste(): void {
    this.store.dispatch(RaetselActions.raetsellisteCleared());
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({raetsel}));
  }  
}
