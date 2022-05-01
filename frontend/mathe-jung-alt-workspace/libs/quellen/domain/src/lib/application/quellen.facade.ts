import { Injectable } from '@angular/core';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { select, Store } from '@ngrx/store';

import * as QuellenActions from '../+state/quelle/quelle.actions';
import * as fromQuelle from '../+state/quelle/quelle.reducer';
import * as QuelleSelectors from '../+state/quelle/quelle.selectors';

@Injectable({ providedIn: 'root' })
export class QuellenFacade {

  loaded$ = this.store.pipe(select(QuelleSelectors.getQuelleLoaded));
  quelleList$ = this.store.pipe(select(QuelleSelectors.getAllQuelle));
  selectedQuelle$ = this.store.pipe(select(QuelleSelectors.getSelected));

  constructor(private store: Store<fromQuelle.QuellePartialState>) {}

  findQuelle(suchfilter: Suchfilter): void {
    this.store.dispatch(QuellenActions.findQuelle({suchfilter}));
  }
}
