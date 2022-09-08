import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs/operators';
import * as RaetselActions from './raetsel.actions';
import { RaetselHttpService } from '../../infrastructure/raetsel.http.service';
import { MessageService, SafeNgrxService, noopAction, FileService } from '@mja-workspace/shared/util-mja';
import { Router } from '@angular/router';
import { SuchfilterFacade } from '@mja-workspace/suchfilter/domain';
import { AuthHttpService } from '@mja-workspace/shared/auth/domain';

@Injectable()
export class RaetselEffects {

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselHttpService.loadPage(action.suchfilter,
          { pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection }).pipe(
            tap(() => this.suchfilterFacade.sucheFinished(action.kontext)),
            map((suchergebnis) => RaetselActions.findRaetselSuccess({ suchergebnis }))
          ), 'Ups, beim Suchen nach Rätseln ist etwas schiefgegangen', noopAction()
      )
    ));

  selectRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselSelected),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselHttpService.findById(action.raetsel.id).pipe(
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
        this.raetselHttpService.saveRaetsel(action.editRaetselPayload, action.csrfToken).pipe(
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

  generateRaetselPNGs$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.generateRaetselPNGs),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselHttpService.generateRaetselPNGs(action.raetselId, action.layoutAntwortvorschlaege).pipe(
          map((images) =>
            RaetselActions.raetselPNGsGenerated({ images })
          )
        ), 'Ups, beim Generieren der PNGs für das Rätsel ist etwas schiefgegangen', noopAction()
      )
    )
  );

  generateRaetselPDF$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.generateRaetselPDF),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselHttpService.generateRaetselPDF(action.raetselId, action.layoutAntwortvorschlaege).pipe(
          map((generatedPDF) =>
            RaetselActions.raetselPDFGenerated({ pdf: generatedPDF })
          )
        ), 'Ups, beim Generieren des PDFs mit dem Rätsel ist etwas schiefgegangen', noopAction()
      )
    )
  );

  downloadPDF$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselPDFGenerated),
      tap((action) => this.fileService.downloadPdf(action.pdf.fileData, action.pdf.fileName)),
    ), { dispatch: false });


  constructor(
    private actions$: Actions,
    private authService: AuthHttpService,
    private raetselHttpService: RaetselHttpService,
    private suchfilterFacade: SuchfilterFacade,
    private safeNgrx: SafeNgrxService,
    private messageService: MessageService,
    private fileService: FileService,
    private router: Router
  ) { }
}
