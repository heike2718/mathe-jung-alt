import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { filter, map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';
import { RaetselFacade } from '../../application/reaetsel.facade';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { Router } from '@angular/router';
import { SuchfilterFacade, SuchfilterWithStatus } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { noop } from 'rxjs';

@Injectable()
export class RaetselEffects {

  prepareSearch$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.prepareSearch),
      withLatestFrom(this.suchfilterFacade.suchfilterWithStatus$),
      this.safeNgrx.safeSwitchMap(([_action, suchfilter]) =>
        this.raetselDataService.countRaetsel(suchfilter.suchfilter).pipe(
          tap((anzahl) => this.raetselFacade.startSearch(anzahl)),
          map(() => noopAction())
        ), 'Ups, beim Zählen der Rätsel ist etwas schiefgegangen', noopAction()
      )
    )
  );

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      withLatestFrom(
        this.suchfilterFacade.suchfilterWithStatus$,
        this.raetselFacade.paginationState$
      ),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap(([_action, suchfilterWithStatus, paginationState]) =>
        this.raetselDataService.loadPage(suchfilterWithStatus, 
          { pageIndex: paginationState.pageIndex, pageSize: paginationState.pageSize, sortDirection: paginationState.sortDirection }).pipe(
          map((raetsel) => RaetselActions.findRaetselSuccess({ raetsel }))
        ), 'Ups, beim Suchen nach Rätseln ist etwas schiefgegangen', noopAction()
      )
    ));

  saveRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.startSaveRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.saveRaetsel(action.editRaetselPayload).pipe(
          map((raetselDetails) => RaetselActions.raetselSaved({ raetselDetails, successMessage: 'Das Raetsel wurde erfolgreich gespeichert: uuid=' + raetselDetails.id }))
        ), 'Ups, beim Speichern des Rätsels ist etwas schiefgegangen', noopAction()
      )
    )
  );

  selectRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselSelected),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.findById(action.raetsel.id).pipe(
          map((raetselDetails) =>
            RaetselActions.raetselDetailsLoaded({ raetselDetails })
          )
        ), 'Ups, beim Laden der Details ist etwas schiefgegangen', noopAction()
      )
    )
  );

  navigateToDetailsAfterDetailsLoaded$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselDetailsLoaded),
      tap(() => this.router.navigateByUrl('raetsel/details')),
    ), { dispatch: false });


  navigateToRaetselEditor$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.startEditRaetsel),
      tap(() => this.router.navigateByUrl('raetseleditor')),
    ), { dispatch: false });

  constructor(
    private actions$: Actions,
    private raetselDataService: RaetselDataService,
    private raetselFacade: RaetselFacade,
    private suchfilterFacade: SuchfilterFacade,
    private safeNgrx: SafeNgrxService,
    private router: Router
  ) { }
}
