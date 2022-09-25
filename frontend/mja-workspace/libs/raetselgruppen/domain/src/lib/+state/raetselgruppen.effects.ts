import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthHttpService } from '@mja-workspace/shared/auth/domain';
import { MessageService, noopAction, SafeNgrxService } from '@mja-workspace/shared/util-mja';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs/operators';
import { RaetselgruppenHttpService } from '../infrastructure/raetselgruppen.http.service';
import * as RaetselgruppenActions from './raetselgruppen.actions';

@Injectable()
export class RaetselgruppenEffects {

    constructor(
        private actions$: Actions,
        private authService: AuthHttpService,
        private raetselHttpService: RaetselgruppenHttpService,
        private safeNgrx: SafeNgrxService,
        private messageService: MessageService,
        private router: Router
    ) { }

    navigateToRaetselgruppeEditor$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.editRaetselgruppe),
            tap((_action) => {
                // console.log('navigate to raetselgruppe-editor');
                this.router.navigateByUrl('raetselgruppe-editor');
            }),
        ), { dispatch: false });


    loadPage$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.suchparameterChanged),
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.findGruppen(action.suchparameter).pipe(
                    map((treffer) =>
                        RaetselgruppenActions.pageLoaded({ treffer: treffer })
                    )
                ), 'Ups, beim Laden der Rätselgruppen ist etwas schiefgegangen', noopAction()
            )
        )
    );

    selectRaetselgruppe$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.selectRaetselgruppe),
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.findById(action.raetselgruppe.id).pipe(
                    map((raetraetselgruppeDetails) =>
                        RaetselgruppenActions.raetselgruppeDetailsLoaded({ raetraetselgruppeDetails })
                    )
                ), 'Ups, beim Laden der Details ist etwas schiefgegangen', noopAction()
            )
        )
    );

    showDetailsAfterLoaded$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.raetselgruppeDetailsLoaded),
            tap(() => {
                this.router.navigateByUrl('raetselgruppen/details');
            }),
        ), { dispatch: false });

    getCsrfToken$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.startSaveRaetselgruppe),
            // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
            this.safeNgrx.safeSwitchMap((action) =>
                this.authService.getCsrfToken().pipe(
                    map((token) => RaetselgruppenActions.saveRaetselgruppe({ editRaetselgruppePayload: action.editRaetselgruppePayload, csrfToken: token }))
                ), 'Ups, beim Speichern der Rätselgruppe ist etwas schiefgegangen', noopAction()
            )
        )
    );

    saveRaetsel$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.saveRaetselgruppe),
            // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.saveRaetselgruppe(action.editRaetselgruppePayload, action.csrfToken).pipe(
                    tap((raetselgruppe) => this.messageService.info('Die Rätselgruppe wurde erfolgreich gespeichert: uuid=' + raetselgruppe.id)),
                    map((raetselgruppe) => RaetselgruppenActions.raetselgruppeSaved({ raetselgruppe }))
                ), 'Ups, beim Speichern der Rätselgruppe ist etwas schiefgegangen', noopAction()
            )
        )
    );

    saveRaetselgruppenelement$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.saveRaetselgruppenelement),
            // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.saveRaetselgruppenelement(action.raetselgruppeID, action.payload).pipe(
                    tap(() => this.messageService.info('Das Rätselgruppenelement wurde erfolgreich gespeichert')),
                    map((raetraetselgruppeDetails) => RaetselgruppenActions.raetselgruppenelementeChanged({ raetraetselgruppeDetails }))
                ), 'Ups, beim Speichern des Rätselgruppenelements ist etwas schiefgegangen', noopAction()
            )
        )
    );

    deleteRaetselgruppenelement$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.deleteRaetselgruppenelement),
            // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.deleteRaetselgruppenelement(action.raetselgruppeID, action.payload).pipe(
                    tap(() => this.messageService.info('Das Rätselgruppenelement wurde erfolgreich gelöscht')),
                    map((raetraetselgruppeDetails) => RaetselgruppenActions.raetselgruppenelementeChanged({ raetraetselgruppeDetails }))
                ), 'Ups, beim Löschen des Rätselgruppenelements ist etwas schiefgegangen', noopAction()
            )
        )
    );


}