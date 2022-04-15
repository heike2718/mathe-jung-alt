import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadDeskriptoren, deskriptorAddedToSearchList, deskriptorRemovedFromSearchList } from '../+state/deskriptor/deskriptor.actions';
import * as fromDeskriptor from '../+state/deskriptor/deskriptor.reducer';
import * as DeskriptorSelectors from '../+state/deskriptor/deskriptor.selectors';
import { Deskriptor } from '../entities/deskriptor';

@Injectable({ providedIn: 'root' })
export class DeskriptorenSearchFacade {

  loaded$ = this.store.pipe(select(DeskriptorSelectors.getDeskriptorLoaded));
  deskriptorList$ = this.store.pipe(select(DeskriptorSelectors.getAllDeskriptor));
  selectedDeskriptor$ = this.store.pipe(select(DeskriptorSelectors.getSelected));
  suchliste$ = this.store.pipe(select(DeskriptorSelectors.getSuchliste));
  restliste$ = this.store.pipe(select(DeskriptorSelectors.getRestliste));

  constructor(private store: Store<fromDeskriptor.DeskriptorPartialState>) { }

  load(): void {
    this.store.dispatch(loadDeskriptoren());
  }

  addToSearchlist(deskriptor: Deskriptor): void {

    this.store.dispatch(deskriptorAddedToSearchList({deskriptor}));

  }

  removeFromSearchlist(deskriptor: Deskriptor): void {
    this.store.dispatch(deskriptorRemovedFromSearchList({deskriptor}));
  }
}
