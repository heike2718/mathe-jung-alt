import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Suchfilter, SuchfilterFacade, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { select, Store } from '@ngrx/store';
import { tap } from 'rxjs';

import * as QuellenActions from '../+state/quelle/quelle.actions';
import * as fromQuelle from '../+state/quelle/quelle.reducer';
import * as QuelleSelectors from '../+state/quelle/quelle.selectors';
import { Quelle } from '../entities/quelle';

@Injectable({ providedIn: 'root' })
export class QuellenFacade {

  loaded$ = this.store.pipe(select(QuelleSelectors.getQuelleLoaded));
  quellenList$ = this.store.pipe(select(QuelleSelectors.getAllQuellen));
  selectedQuelle$ = this.store.pipe(select(QuelleSelectors.getSelected));
  adminQuelle$ = this.store.pipe(select(QuelleSelectors.getAdminQuelle));
  page$ = this.store.pipe(select(QuelleSelectors.getPage));
  isGewaehlteQuelleAdminQuelle$ = this.store.pipe(select(QuelleSelectors.isGewaehlteQuelleAdminQuelle));

  public lastSuchfilter: Suchfilter = {
    kontext: 'QUELLEN',
    deskriptoren: [],
    suchstring: ''
  };

  #deskriptorenLoaded = false;

  constructor(private store: Store<fromQuelle.QuellePartialState>, private suchfilterFacade: SuchfilterFacade, private router: Router) {

    this.suchfilterFacade.deskriptorenLoaded$.pipe(
      tap((loaded) => this.#deskriptorenLoaded = loaded)
    ).subscribe();

    this.suchfilterFacade.selectedSuchfilter$.pipe(tap((suchfilter: Suchfilter | undefined) => {
      if (suchfilter && suchfilter.kontext === 'QUELLEN') {
        this.lastSuchfilter = suchfilter
      }
    })).subscribe();

  }

  checkOrLoadDeskriptoren(): void {
    if (!this.#deskriptorenLoaded) {
      this.suchfilterFacade.loadDeskriptoren();
    }
  }

  findQuellen(suchfilter: Suchfilter): void {

    if (suchkriterienVorhanden(suchfilter)) {
      this.store.dispatch(QuellenActions.findQuellen({ suchfilter }));
    }
  }

  loadQuelle(uuid: string) {
    if (uuid) {
      this.store.dispatch(QuellenActions.loadQuelle({ uuid }));
    }
  }

  slicePage(sortDirection = 'asc', pageIndex = 0, pageSize = 10): void {
    this.store.dispatch(QuellenActions.selectPage({ sortDirection, pageIndex, pageSize }))
  }

  selectQuelle(quelle: Quelle): void {
    this.store.dispatch(QuellenActions.quelleSelected({ quelle }));
  }

  navigateToQuellensuche(): void {
    this.router.navigateByUrl('quellen');
  }
}
