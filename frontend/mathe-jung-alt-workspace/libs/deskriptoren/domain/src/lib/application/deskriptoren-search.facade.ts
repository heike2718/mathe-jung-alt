import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import * as fromDeskriptor from '../+state/deskriptor/deskriptor.reducer';
import * as DeskriptorActions from '../+state/deskriptor/deskriptor.actions'
import * as DeskriptorSelectors from '../+state/deskriptor/deskriptor.selectors';

@Injectable({ providedIn: 'root' })
export class DeskriptorenSearchFacade {

  loaded$ = this.store.pipe(select(DeskriptorSelectors.getDeskriptorLoaded));
  deskriptorList$ = this.store.pipe(select(DeskriptorSelectors.getAllDeskriptor));
  selectedDeskriptor$ = this.store.pipe(select(DeskriptorSelectors.getSelected));  

  constructor(private store: Store<fromDeskriptor.DeskriptorenPartialState>) { }

  load(): void {
    this.store.dispatch(DeskriptorActions.loadDeskriptoren());
  }
}
