import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { QuellenFacade } from '@mja-workspace/quellen/domain';
import { SelectableItem } from '@mja-workspace/shared/util-mja';
import { Deskriptor, filterByKontext, getDifferenzmenge, PageDefinition, Suchfilter, SuchfilterFacade, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { select, Store } from '@ngrx/store';
import { combineLatest, filter, Observable, tap } from 'rxjs';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { EditRaetselPayload, initialRaetselDetails, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel, RaetselDetails, RaetselDetailsContent } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  paginationState$ = this.store.pipe(select(RaetselSelectors.getPaginationState));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));
  isGeneratingOutput$ = this.store.pipe(select(RaetselSelectors.isGeneratingOutput));
  editorContent$: Observable<RaetselDetailsContent | undefined> = this.store.pipe(select(RaetselSelectors.getDetailsContent));
  
  lastSuchfilter: Suchfilter = {
    kontext: 'RAETSEL',
    deskriptoren: [],
    suchstring: ''
  };

  #deskriptorenLoaded = false;
  #raetselDeskriptoren: Deskriptor[] = [];
  #adminQuelleId: string | undefined;

  constructor(private store: Store<fromRaetsel.RaetselPartialState>,
    private suchfilterFacade: SuchfilterFacade,
    private quellenFacade: QuellenFacade,
    private router: Router) {

    combineLatest([
      this.suchfilterFacade.deskriptorenLoaded$,
      this.suchfilterFacade.allDeskriptoren$,
    ]).subscribe(([deskriptorenLoaded, allDeskriptoren]) => {
      if (deskriptorenLoaded) {
        this.#raetselDeskriptoren = filterByKontext('RAETSEL', allDeskriptoren);
      }
    });

    this.suchfilterFacade.selectedSuchfilter$.pipe(tap((suchfilter: Suchfilter | undefined) => {
      if (suchfilter && suchfilter.kontext === 'RAETSEL') {
        this.lastSuchfilter = suchfilter
      }
    })).subscribe();

    this.quellenFacade.adminQuelle$.subscribe(
      quelle => {
        if (quelle) {
          this.#adminQuelleId = quelle.id;
        } else {
          this.#adminQuelleId = undefined;
        }
      }
    );
  }

  checkOrLoadDeskriptoren(): void {
    if (!this.#deskriptorenLoaded) {
      this.suchfilterFacade.loadDeskriptoren();
    }
  }

  startSearch(anzahlTreffer: number, suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.raetselCounted({ anzahl: anzahlTreffer }));
    this.#findRaetsel(suchfilter, pageDefinition);
  }

  /*  Setzt die Suchkette mit serverseitiger Pagination in Gang. Hierzu wird ein select count mit dem Suchfilter abgesetzt */
  triggerSearch(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {

    if (suchkriterienVorhanden(suchfilter)) {
      this.store.dispatch(RaetselActions.selectPage({ pageDefinition }));
      this.store.dispatch(RaetselActions.prepareSearch({ suchfilter, pageDefinition }));
    }
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.store.dispatch(RaetselActions.raetselSelected({ raetsel }));
  }

  generiereRaetselOutput(raetselId: string, outputFormat: LATEX_OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
    this.store.dispatch(RaetselActions.generateOutput({ raetselId, outputFormat, layoutAntwortvorschlaege }));
  }


  clearTrefferliste(): void {
    this.store.dispatch(RaetselActions.raetsellisteCleared());
  }

  createAndEditRaetsel(): void {
    let raetselDetails: RaetselDetails;
    if (this.#adminQuelleId) {
      raetselDetails = { ...initialRaetselDetails, quelleId: this.#adminQuelleId };
      this.editRaetsel(raetselDetails);
    } else {
      this.router.navigateByUrl('/quellen');
    }
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

    const content: RaetselDetailsContent = {
      raetsel: raetselDetails,
      quelleId: raetselDetails.quelleId,
      kontext: 'RAETSEL',
      selectableDeskriptoren: selectableDeskriptoren
    };

    this.store.dispatch(RaetselActions.editRaetsel({ raetselDetailsContent: content }));
  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.store.dispatch(RaetselActions.startSaveRaetsel({ editRaetselPayload }));
  }

  cancelEditRaetsel(): void {
    this.store.dispatch(RaetselActions.cancelEdit());
  }

  // ///// private methods //////

  #findRaetsel(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.findRaetsel({ suchfilter, pageDefinition, kontext: 'RAETSEL' }));
  }
}
