import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { anonymousSession, isAnonymousSession, Session, User } from '@mja-workspace/shared/auth/domain';
import { Configuration, SharedConfigService, STORAGE_KEY_SESSION } from '@mja-workspace/shared/util-configuration';
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
  page$ = this.store.pipe(select(QuelleSelectors.getPage));

  #storagePrefix!: string;

  public lastSuchfilter: Suchfilter = {
    kontext: 'QUELLEN',
    deskriptoren: [],
    suchstring: ''
  };

  #deskriptorenLoaded = false;

  constructor(private store: Store<fromQuelle.QuellePartialState>,
    @Inject(SharedConfigService) private configuration: Configuration,
    private suchfilterFacade: SuchfilterFacade,
    private router: Router) {

    this.#storagePrefix = configuration.storagePrefix;

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

  loadQuelleAdmin(): void {
    this.store.dispatch(QuellenActions.loadQuelleLoggedInUser());
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
    this.router.navigateByUrl('quellen/uebersicht');
  }
}
