import { HttpClient, HttpContext } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { coreStatistikActions } from "./core-statistik.actions";
import { concatMap, map } from "rxjs";
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
            ofType(coreStatistikActions.load_anzahl_raetsel_public),
            concatMap(() =>
                this.#httpClient.get<AnzahlabfrageErgebnis>('/mja-api/raetsel/public/anzahl/v1', { context: new HttpContext().set(SILENT_LOAD_CONTEXT, true) })
            ),
            map((payload: AnzahlabfrageErgebnis) => coreStatistikActions.anzahl_raetsel_public_loaded({ payload }))
        )
    });
}