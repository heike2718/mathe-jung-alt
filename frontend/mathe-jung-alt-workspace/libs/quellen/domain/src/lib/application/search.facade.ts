import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadQuelle } from '../+state/quelle/quelle.actions';
import * as fromQuelle from '../+state/quelle/quelle.reducer';
import * as QuelleSelectors from '../+state/quelle/quelle.selectors';

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  loaded$ = this.store.pipe(select(QuelleSelectors.getQuelleLoaded));
  quelleList$ = this.store.pipe(select(QuelleSelectors.getAllQuelle));
  selectedQuelle$ = this.store.pipe(select(QuelleSelectors.getSelected));

  constructor(private store: Store<fromQuelle.QuellePartialState>) {}

  load(): void {
    this.store.dispatch(loadQuelle());
  }
}
