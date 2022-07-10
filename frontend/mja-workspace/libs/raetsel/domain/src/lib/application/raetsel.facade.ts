import { Injectable } from '@angular/core';
import { PageDefinition, Suchfilter, SuchfilterFacade, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { select, Store } from '@ngrx/store';
import { filter, tap } from 'rxjs';

import * as RaetselActions from '../+state/raetsel/raetsel.actions';
import * as fromRaetsel from '../+state/raetsel/raetsel.reducer';
import * as RaetselSelectors from '../+state/raetsel/raetsel.selectors';
import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselSearchFacade {

  loaded$ = this.store.pipe(select(RaetselSelectors.getRaetselLoaded));
  selectedRaetsel$ = this.store.pipe(select(RaetselSelectors.getSelected));
  page$ = this.store.pipe(select(RaetselSelectors.getPage));
  paginationState$ = this.store.pipe(select(RaetselSelectors.getPaginationState));
  raetselDetails$ = this.store.pipe(select(RaetselSelectors.getRaetselDetails));
  isGeneratingOutput$ = this.store.pipe(select(RaetselSelectors.isGeneratingOutput));

  public lastSuchfilter: Suchfilter = {
    kontext: 'RAETSEL',
    deskriptoren: [],
    suchstring: ''
  };

  #deskriptorenLoaded = false;

  constructor(private store: Store<fromRaetsel.RaetselPartialState>, private suchfilterFacade: SuchfilterFacade) {

    this.suchfilterFacade.deskriptorenLoaded$.pipe(
      tap((loaded) => this.#deskriptorenLoaded = loaded)
    ).subscribe();

    this.suchfilterFacade.selectedSuchfilter$.pipe(tap((suchfilter: Suchfilter | undefined) => {
      if (suchfilter && suchfilter.kontext === 'RAETSEL') {
        this.lastSuchfilter = suchfilter
      }
    })).subscribe();
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

  // ///// private methods //////

  #findRaetsel(suchfilter: Suchfilter, pageDefinition: PageDefinition): void {
    this.store.dispatch(RaetselActions.findRaetsel({ suchfilter, pageDefinition, kontext: 'RAETSEL' }));
  }
}
