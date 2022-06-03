import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Quelle, QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { PageDefinition, Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { MessageService } from '@mathe-jung-alt-workspace/shared/ui-messaging';
import { select, Store } from '@ngrx/store';
import { filter, tap } from 'rxjs';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { EditRaetselPayload, initialRaetselDetails, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel, RaetselDetails } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));
  paginationState$ = this.store.pipe(select(RaetselSelectors.getPaginationState));

  #selectedQuelleId: string | undefined;
  #actuallyEditedRaetsel: RaetselDetails | undefined;

  constructor(private store: Store<fromRaetsel.RaetselPartialState>, private messageService: MessageService, private quellenFacade: QuellenFacade, private router: Router) {

    this.store.pipe(select(RaetselSelectors.getSaveSuccessMessage)).subscribe(
      (message) => {
        if (message) {
          this.messageService.info(message);
        }
      }
    );

    this.quellenFacade.selectedQuelle$.subscribe(
      quelle => {
        if (quelle) {
          this.#selectedQuelleId = quelle.id;
        } else {
          this.#selectedQuelleId = undefined;
        }
      }
    );

    this.store.pipe(select(RaetselSelectors.getRaetselDetails)).subscribe(
      (raetselDetails) => this.#actuallyEditedRaetsel = raetselDetails
    );
  }

  /*  Setzt die Suchkette mit serverseitiger Pagination in Gang. */
  triggerSearch(pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.selectPage({ pageDefinition }));
    this.store.dispatch(RaetselActions.prepareSearch());
  }

  startSearch(anzahlTreffer: number): void {
    this.store.dispatch(RaetselActions.raetselCounted({ anzahl: anzahlTreffer }));
    this.findRaetsel();
  }

  findRaetsel(): void {
    this.store.dispatch(RaetselActions.findRaetsel());
  }

  clearTrefferliste(): void {
    this.store.dispatch(RaetselActions.raetsellisteCleared());
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({ raetsel }));
  }

  startCreateRaetsel(): void {
    let raetselDetails: RaetselDetails;
    if (this.#selectedQuelleId) {
      if (this.#actuallyEditedRaetsel) {
        raetselDetails = this.#actuallyEditedRaetsel;
      } else {
        raetselDetails = initialRaetselDetails;
      }
      raetselDetails = { ...raetselDetails, quelleId: this.#selectedQuelleId };
      this.store.dispatch(RaetselActions.raetselDetailsLoaded({ raetselDetails }));
    } else {
      this.router.navigateByUrl('/quellen');
    }
  }

  cancelEditRaetsel(): void {
    this.#actuallyEditedRaetsel = undefined;
    this.store.dispatch(RaetselActions.cancelEdit());
  }

  startEditRaetsel(raetselDetails: RaetselDetails): void {
    this.store.dispatch(RaetselActions.startEditRaetsel({ raetselDetails }));
  }

  generateRaetsel(raetselId: string, outputFormat: LATEX_OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
    this.store.dispatch(RaetselActions.generateOutput({ raetselId, outputFormat, layoutAntwortvorschlaege }));
  }

  cacheRaetselDetails(raetselDetails: RaetselDetails): void {
    this.store.dispatch(RaetselActions.cacheRaetselDetails({ raetselDetails }));
  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.store.dispatch(RaetselActions.startSaveRaetsel({ editRaetselPayload }));
  }
}
