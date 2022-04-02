import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { loadMedien } from '../+state/medien/medien.actions';
import * as fromMedien from '../+state/medien/medien.reducer';
import * as MedienSelectors from '../+state/medien/medien.selectors';

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  loaded$ = this.store.pipe(select(MedienSelectors.getMedienLoaded));
  medienList$ = this.store.pipe(select(MedienSelectors.getAllMedien));
  selectedMedien$ = this.store.pipe(select(MedienSelectors.getSelected));

  constructor(private store: Store<fromMedien.MedienPartialState>) {}

  load(): void {
    this.store.dispatch(loadMedien());
  }
}
