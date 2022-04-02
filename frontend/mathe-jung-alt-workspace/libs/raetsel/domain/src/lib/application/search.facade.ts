import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadRaetsel } from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  raetselList$ = this.store.pipe(select(RaetselSelectors.getAllRaetsel));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));

  constructor(private store: Store<fromRaetsel.RaetselPartialState>) { }

  load(): void {
    this.store.dispatch(loadRaetsel());
  }
}
