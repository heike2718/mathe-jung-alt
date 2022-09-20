import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthHttpService } from '@mja-workspace/shared/auth/domain';
import { noopAction, SafeNgrxService } from '@mja-workspace/shared/util-mja';
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


}