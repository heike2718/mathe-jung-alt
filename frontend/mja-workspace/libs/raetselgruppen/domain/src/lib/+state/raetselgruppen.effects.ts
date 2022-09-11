import { Injectable } from '@angular/core';
import { noopAction, SafeNgrxService } from '@mja-workspace/shared/util-mja';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs/operators';
import { RaetselgruppenHttpService } from '../infrastructure/raetselgruppen.http.service';
import * as RaetselgruppenActions from './raetselgruppen.actions';

@Injectable()
export class RaetselgruppenEffects {

    constructor(
        private actions$: Actions,
        private raetselHttpService: RaetselgruppenHttpService,
        private safeNgrx: SafeNgrxService
    ) { }

    loadPage$ = createEffect(() =>
        this.actions$.pipe(
            ofType(RaetselgruppenActions.suchparameterChanged),
            this.safeNgrx.safeSwitchMap((action) =>
                this.raetselHttpService.findGruppen(action.suchparameter).pipe(
                    tap((treffer) => console.log('response erhalten: Anzahl gesamt=' + treffer.anzahlTreffer)),
                    map((treffer) =>
                        RaetselgruppenActions.pageLoaded({ treffer: treffer })
                    )
                ), 'Ups, beim Laden der Rätselgruppen ist etwas schiefgegangen', noopAction()
            )
        )
    );


}