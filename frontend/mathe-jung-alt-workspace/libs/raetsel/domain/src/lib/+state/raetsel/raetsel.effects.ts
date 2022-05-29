import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, switchMap, tap } from 'rxjs/operators';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';
import { RaetselFacade } from '../../application/reaetsel.facade';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { Router } from '@angular/router';

@Injectable()
export class RaetselEffects {

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.findRaetsel(action.suchfilter).pipe(
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

  selectPage$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.selectPage),
      // switchMap, damit das Pagen einer früheren Page-Auswahl gecanceled wird, sobald eine neue Page ausgewählt wurde
      switchMap((action) =>
        this.raetselFacade.raetselList$.pipe(
          map((raetsel) => RaetselActions.pageSelected({
            raetsel: raetsel.slice(action.pageIndex * action.pageSize, (action.pageIndex + 1) * action.pageSize)
          }))
        )
      )
    ));

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
    private safeNgrx: SafeNgrxService,
    private router: Router
  ) { }
}
