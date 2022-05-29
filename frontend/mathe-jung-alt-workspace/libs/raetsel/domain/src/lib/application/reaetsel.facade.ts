import { Injectable } from '@angular/core';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { MessageService } from '@mathe-jung-alt-workspace/shared/ui-messaging';
import { select, Store } from '@ngrx/store';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { EditRaetselPayload, Raetsel, RaetselDetails } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  raetselList$ = this.store.pipe(select(RaetselSelectors.getAllRaetsel));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));

  constructor(private store: Store<fromRaetsel.RaetselPartialState>, private messageService: MessageService) {

    this.store.pipe(select(RaetselSelectors.getSaveSuccessMessage)).subscribe(
      (message) => {
        if (message) {
          this.messageService.info(message);
        }
      }
    );
  }

  findRaetsel(suchfilter: Suchfilter): void {
    this.store.dispatch(RaetselActions.findRaetsel({ suchfilter }));
  }

  slicePage(sortDirection = 'asc', pageIndex = 0, pageSize = 10): void {
    this.store.dispatch(RaetselActions.selectPage({ sortDirection, pageIndex, pageSize }))
  }

  clearTrefferliste(): void {
    this.store.dispatch(RaetselActions.raetsellisteCleared());
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({ raetsel }));
  }

  startEditRaetsel(raetselDetails: RaetselDetails): void {
    this.store.dispatch(RaetselActions.startEditRaetsel({ raetselDetails }));
  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.store.dispatch(RaetselActions.startSaveRaetsel({ editRaetselPayload }));
  }
}
