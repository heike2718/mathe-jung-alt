import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { RaetselDetails } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselEditFacade {

    constructor(private store: Store<fromRaetsel.RaetselPartialState>) { }




  startEditRaetsel(raetselDetails: RaetselDetails): void {
    this.store.dispatch(RaetselActions.startEditRaetsel({raetselDetails}));
  }

}


