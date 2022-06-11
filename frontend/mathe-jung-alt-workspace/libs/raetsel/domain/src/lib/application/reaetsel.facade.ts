import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Deskriptor, filterByKontext, getDifferenzmenge } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { PageDefinition, Suchfilter, SuchfilterFacade } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { select, Store } from '@ngrx/store';
import { SelectableItem } from 'libs/shared/ui-components/src/lib/select-items/select-items.model';
import { combineLatest, filter, Observable, tap } from 'rxjs';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { EditRaetselPayload, initialRaetselDetails, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel, RaetselDetails, RaetselEditorContent } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));
  paginationState$ = this.store.pipe(select(RaetselSelectors.getPaginationState));
  editorContent$: Observable<RaetselEditorContent | undefined> = this.store.pipe(select(RaetselSelectors.getEditorContent));
  generatingOutput$: Observable<boolean> = this.store.pipe(select(RaetselSelectors.generatingOutput));

  #selectedQuelleId: string | undefined;
  #raetselDeskriptoren: Deskriptor[] = [];

  constructor(private store: Store<fromRaetsel.RaetselPartialState>,
    private quellenFacade: QuellenFacade,
    private suchfilterFacade: SuchfilterFacade,
    private router: Router) {

    this.quellenFacade.selectedQuelle$.subscribe(
      quelle => {
        if (quelle) {
          this.#selectedQuelleId = quelle.id;
        } else {
          this.#selectedQuelleId = undefined;
        }
      }
    );

    combineLatest([
      this.suchfilterFacade.deskriptorenLoaded$,
      this.suchfilterFacade.allDeskriptoren$,
    ]).subscribe(([deskriptorenLoaded, allDeskriptoren]) => {
      if (deskriptorenLoaded) {
        this.#raetselDeskriptoren = filterByKontext('RAETSEL', allDeskriptoren);
      }
    });
  }

  /*  Setzt die Suchkette mit serverseitiger Pagination in Gang. Hierzu wird ein select count mit dem Suchfilter abgesetzt */
  triggerSearch(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.selectPage({ pageDefinition }));
    this.store.dispatch(RaetselActions.prepareSearch({suchfilter, pageDefinition}));
  }

  startSearch(anzahlTreffer: number, suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.raetselCounted({ anzahl: anzahlTreffer }));
    this.#findRaetsel(suchfilter, pageDefinition);
  }

  clearTrefferliste(): void {
    this.store.dispatch(RaetselActions.raetsellisteCleared());
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({ raetsel }));
  }

  createAndEditRaetsel(): void {
    let raetselDetails: RaetselDetails;
    if (this.#selectedQuelleId) {
      raetselDetails = { ...initialRaetselDetails, quelleId: this.#selectedQuelleId };
      this.editRaetsel(raetselDetails);
    } else {
      this.router.navigateByUrl('/quellen');
    }
  }

  showDetails(): void {
    this.store.dispatch(RaetselActions.showRaetselDetails());
  }

  cancelEditRaetsel(): void {
    this.store.dispatch(RaetselActions.cancelEdit());
  }

  editRaetsel(raetselDetails: RaetselDetails): void {

    const vorrat: Deskriptor[] = getDifferenzmenge(this.#raetselDeskriptoren, raetselDetails.deskriptoren);
    const selectableDeskriptoren: SelectableItem[] = [];

    raetselDetails.deskriptoren.forEach(deskriptor => {
      selectableDeskriptoren.push({ id: deskriptor.id, name: deskriptor.name, selected: true });
    });

    vorrat.forEach(deskriptor => {
      selectableDeskriptoren.push({ id: deskriptor.id, name: deskriptor.name, selected: false });
    });

    const content: RaetselEditorContent = {
      raetsel: raetselDetails,
      quelleId: this.#selectedQuelleId,
      kontext: 'RAETSEL',
      selectableDeskriptoren: selectableDeskriptoren
    };

    this.store.dispatch(RaetselActions.editRaetsel({ raetselEditorContent: content }));
  }

  generiereRaetselOutput(raetselId: string, outputFormat: LATEX_OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
    this.store.dispatch(RaetselActions.generateOutput({ raetselId, outputFormat, layoutAntwortvorschlaege }));
  }

  cacheRaetselDetails(raetselDetails: RaetselDetails): void {
    this.store.dispatch(RaetselActions.cacheRaetselDetails({ raetselDetails }));
  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.store.dispatch(RaetselActions.startSaveRaetsel({ editRaetselPayload }));
  }

  // ###################################################################


  #findRaetsel(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.findRaetsel({suchfilter, pageDefinition}));
  }
}
