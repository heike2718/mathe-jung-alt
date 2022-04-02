import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadBild } from '../+state/bild/bild.actions';
import * as fromBild from '../+state/bild/bild.reducer';
import * as BildSelectors from '../+state/bild/bild.selectors';

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  loaded$ = this.store.pipe(select(BildSelectors.getBildLoaded));
  bildList$ = this.store.pipe(select(BildSelectors.getAllBild));
  selectedBild$ = this.store.pipe(select(BildSelectors.getSelected));

  constructor(private store: Store<fromBild.BildPartialState>) {}

  load(): void {
    this.store.dispatch(loadBild());
  }
}
