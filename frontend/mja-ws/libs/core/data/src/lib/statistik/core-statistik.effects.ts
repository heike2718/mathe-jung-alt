import { HttpClient, HttpContext } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { coreStatistikActions } from "./core-statistik.actions";
import { switchMap, map } from "rxjs";
import { AnzahlabfrageErgebnis } from "@mja-ws/core/model";
import { SILENT_LOAD_CONTEXT } from "@mja-ws/shared/messaging/api";

@Injectable({
    providedIn: 'root'
})
export class CoreStatistikEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);

    loadAnzahlPublicRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(coreStatistikActions.lOAD_ANZAHL_RAETSEL_PUBLIC),
            switchMap(() =>
                this.#httpClient.get<AnzahlabfrageErgebnis>('/mja-api/public/raetsel/anzahl/v1', { context: new HttpContext().set(SILENT_LOAD_CONTEXT, true) })
            ),
            map((payload: AnzahlabfrageErgebnis) => coreStatistikActions.aNZAHL_RAETSEL_PUBLIC_LOADED({ payload }))
        )
    });
}