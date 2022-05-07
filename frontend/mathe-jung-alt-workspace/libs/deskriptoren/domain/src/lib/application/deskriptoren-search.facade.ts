import { Injectable } from '@angular/core';
import { Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { select, Store } from '@ngrx/store';

import * as fromDeskriptor from '../+state/deskriptor/deskriptor.reducer';
import * as DeskriptorActions from '../+state/deskriptor/deskriptor.actions'
import * as DeskriptorSelectors from '../+state/deskriptor/deskriptor.selectors';
import { Deskriptor } from '../entities/deskriptor';

@Injectable({ providedIn: 'root' })
export class DeskriptorenSearchFacade {

  loaded$ = this.store.pipe(select(DeskriptorSelectors.getDeskriptorLoaded));
  deskriptorList$ = this.store.pipe(select(DeskriptorSelectors.getAllDeskriptor));
  selectedDeskriptor$ = this.store.pipe(select(DeskriptorSelectors.getSelected));
  suchliste$ = this.store.pipe(select(DeskriptorSelectors.getSuchliste));
  restliste$ = this.store.pipe(select(DeskriptorSelectors.getRestliste));

  constructor(private store: Store<fromDeskriptor.DeskriptorenPartialState>) { }

  load(kontext: Suchkontext): void {
    this.store.dispatch(DeskriptorActions.loadDeskriptoren({ kontext }));
  }

  setSuchkontext(suchkontext: Suchkontext): void {
    this.store.dispatch(DeskriptorActions.setSuchkontext({ kontext: suchkontext }))
  }

  addToSearchlist(deskriptor: Deskriptor): void {
    this.store.dispatch(DeskriptorActions.deskriptorAddedToSearchList({ deskriptor }));
  }

  removeFromSearchlist(deskriptor: Deskriptor): void {
    this.store.dispatch(DeskriptorActions.deskriptorRemovedFromSearchList({ deskriptor }));
  }
}
