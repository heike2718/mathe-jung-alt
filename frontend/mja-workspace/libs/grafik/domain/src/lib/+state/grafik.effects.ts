import { Injectable } from "@angular/core";
import { Message, MessageService, noopAction, SafeNgrxService } from "@mja-workspace/shared/util-mja";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { map, tap } from 'rxjs/operators';
import { GrafikHttpService } from "../infrastructure/grafik.http.service";
import * as GrafikActions from './grafik.actions';

@Injectable()
export class GrafikEffects {

    constructor(
        private actions$: Actions,
        private grafikHttpService: GrafikHttpService,
        private messageService: MessageService,
        private ngrxService: SafeNgrxService) { }

    ladeGrafik$ = createEffect(() =>
        this.actions$.pipe(
            ofType(GrafikActions.pruefeGrafik),
            this.ngrxService.safeSwitchMap((action) =>
                this.grafikHttpService.loadGrafik(action.pfad).pipe(
                    map((grafikSearchResult) =>
                        GrafikActions.grafikGeprueft({ grafikSearchResult })
                    )
                ), 'Ups, beim Laden des Grafik ist etwas schiefgegangen', noopAction()
            )
        )
    );

    grafikHochgeladen$ = createEffect(() =>
        this.actions$.pipe(
            ofType(GrafikActions.grafikHochgeladen),
            tap((action) => {
                this.messageService.message(action.message);
            }),
        ), { dispatch: false });
}