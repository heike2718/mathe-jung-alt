import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  raetselList$ = this.store.pipe(select(RaetselSelectors.getAllRaetsel));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));

  constructor(private store: Store<fromRaetsel.RaetselPartialState>) { }

  findRaetsel(filter = ''): void {
    this.store.dispatch(RaetselActions.findRaetsel({ filter }));
  }

  slicePage(sortDirection = 'asc', pageIndex = 0, pageSize = 10): void {
    this.store.dispatch(RaetselActions.selectPage({ sortDirection, pageIndex, pageSize }))
  }
}
