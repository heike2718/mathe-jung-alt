import { Injectable } from '@angular/core';
import { Suchfilter } from '@mja-workspace/suchfilter/domain';
import { select, Store } from '@ngrx/store';

import * as QuellenActions from '../+state/quelle/quelle.actions';
import * as fromQuelle from '../+state/quelle/quelle.reducer';
import * as QuelleSelectors from '../+state/quelle/quelle.selectors';
import { Quelle } from '../entities/quelle';

@Injectable({ providedIn: 'root' })
export class QuellenFacade {
  
  loaded$ = this.store.pipe(select(QuelleSelectors.getQuelleLoaded));
  quellenList$ = this.store.pipe(select(QuelleSelectors.getAllQuellen));
  selectedQuelle$ = this.store.pipe(select(QuelleSelectors.getSelected));
  page$ = this.store.pipe(select(QuelleSelectors.getPage));

  constructor(private store: Store<fromQuelle.QuellePartialState>) { }

  findQuellen(suchfilter: Suchfilter): void {
    this.store.dispatch(QuellenActions.findQuellen({ suchfilter }));
  }

  slicePage(sortDirection = 'asc', pageIndex = 0, pageSize = 10): void {
    this.store.dispatch(QuellenActions.selectPage({ sortDirection, pageIndex, pageSize }))
  }

  selectQuelle(quelle: Quelle): void {
    this.store.dispatch(QuellenActions.quelleSelected({quelle}));
  }
}
