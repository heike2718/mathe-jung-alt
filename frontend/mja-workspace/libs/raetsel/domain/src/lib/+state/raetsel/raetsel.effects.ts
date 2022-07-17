import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs/operators';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';
import { MessageService, SafeNgrxService, noopAction } from '@mja-workspace/shared/util-mja';
import { Router } from '@angular/router';
import { SuchfilterFacade } from '@mja-workspace/suchfilter/domain';
import { RaetselFacade } from '../../application/raetsel.facade';
import { AuthHttpService } from '@mja-workspace/shared/auth/domain';

@Injectable()
export class RaetselEffects {

  prepareSearch$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.prepareSearch),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.countRaetsel(action.suchfilter).pipe(
          tap((anzahl) => this.raetselFacade.startSearch(anzahl, action.suchfilter, action.pageDefinition)),
          map(() => noopAction())
        ), 'Ups, beim Zählen der Rätsel ist etwas schiefgegangen', noopAction()
      )
    )
  );

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.loadPage(action.suchfilter,
          { pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection }).pipe(
            tap(() => this.suchfilterFacade.sucheFinished(action.kontext)),
            map((raetsel) => RaetselActions.findRaetselSuccess({ raetsel }))
          ), 'Ups, beim Suchen nach Rätseln ist etwas schiefgegangen', noopAction()
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

  showDetailsAfterLoaded$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselDetailsLoaded),
      tap(() => {
        this.router.navigateByUrl('raetsel/details');
      }),
    ), { dispatch: false });

  navigateToRaetselEditor$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.editRaetsel),
      tap((_action) => {
        this.router.navigateByUrl('raetseleditor');
      }),
    ), { dispatch: false });

  saveRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.saveRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.saveRaetsel(action.editRaetselPayload, action.csrfToken).pipe(
          map((raetselDetails) => RaetselActions.raetselSaved({
            raetselDetails,
            successMessage: 'Das Raetsel wurde erfolgreich gespeichert: uuid=' + raetselDetails.id,
            insert: action.editRaetselPayload.raetsel.id === 'neu'
          }))
        ), 'Ups, beim Speichern des Rätsels ist etwas schiefgegangen', noopAction()
      )
    )
  );

  getCsrfToken$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.startSaveRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.authService.getCsrfToken().pipe(
          map((token) => RaetselActions.saveRaetsel({ editRaetselPayload: action.editRaetselPayload, csrfToken: token }))
        ), 'Ups, beim Speichern des Rätsels ist etwas schiefgegangen', noopAction()
      )
    )
  );

  showSaveSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselSaved),
      tap((action) => {
        this.messageService.info(action.successMessage);
      }),
    ), { dispatch: false });


  cancelEdit$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.cancelEdit),
      tap(() => this.router.navigateByUrl('raetsel/uebersicht')),
    ), { dispatch: false });

  generiereRaetselOutput$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.generateOutput),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.generiereRaetselOutput(action.raetselId, action.outputFormat, action.layoutAntwortvorschlaege).pipe(
          tap(() => {
            if (action.outputFormat === 'PDF') {
              this.messageService.info('PDF erfolgreich generiert');
            }
          }),
          map((images) =>
            RaetselActions.outputGenerated({ images })
          )
        ), 'Ups, beim Generieren des Rätsels ist etwas schiefgegangen', noopAction()
      )
    )
  );

  constructor(
    private actions$: Actions,
    private authService: AuthHttpService,
    private raetselDataService: RaetselDataService,
    private raetselFacade: RaetselFacade,
    private suchfilterFacade: SuchfilterFacade,
    private safeNgrx: SafeNgrxService,
    private messageService: MessageService,
    private router: Router
  ) { }
}
