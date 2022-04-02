import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadStichwort } from '../+state/stichwort/stichwort.actions';
import * as fromStichwort from '../+state/stichwort/stichwort.reducer';
import * as StichwortSelectors from '../+state/stichwort/stichwort.selectors';

@Injectable({ providedIn: 'root' })
export class StichwortsucheFacade {
  loaded$ = this.store.pipe(select(StichwortSelectors.getStichwortLoaded));
  stichwortList$ = this.store.pipe(select(StichwortSelectors.getAllStichwort));
  selectedStichwort$ = this.store.pipe(select(StichwortSelectors.getSelected));

  constructor(private store: Store<fromStichwort.StichwortPartialState>) {}

  load(): void {
    this.store.dispatch(loadStichwort());
  }
}
