import { Injectable } from "@angular/core";
import { noopAction, SafeNgrxService } from "@mja-workspace/shared/util-mja";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { map } from "rxjs";
import { GrafikDataService } from "../infrastructure/grafik.data.service";
import * as GrafikActions from './grafik.actions';

@Injectable()
export class GrafikEffects {

    constructor(
        private actions$: Actions,
        private grafikDataService: GrafikDataService,
        private ngrxService: SafeNgrxService) { }

    ladeGrafik$ = createEffect(() =>
        this.actions$.pipe(
            ofType(GrafikActions.pruefeGrafik),
            this.ngrxService.safeSwitchMap((action) =>
                this.grafikDataService.loadGrafik(action.pfad).pipe(
                    map((messagePayload) =>
                        GrafikActions.grafikGeprueft({messagePayload: messagePayload})
                    )
                ), 'Ups, beim Laden des Grafik ist etwas schiefgegangen', noopAction()
            )
        )
    );
}