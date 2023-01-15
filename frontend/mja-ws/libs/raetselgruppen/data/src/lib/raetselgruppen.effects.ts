import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { concatMap, map, tap } from "rxjs";
import { RaetselgruppenHttpService } from "./raetselgruppen-http.service";
import { raetselgruppenActions } from "./raetselgruppen.actions";

@Injectable({ providedIn: 'root' })
export class RaetselgruppenEffects {

    #actions = inject(Actions);
    #raetselgruppenHttpService = inject(RaetselgruppenHttpService);
    #router = inject(Router);

    findRaetselgruppen$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(raetselgruppenActions.find_raetselgruppen),
            concatMap((action) => this.#raetselgruppenHttpService.findRaetselgruppen(action.raetselgruppenSuchparameter, action.pageDefinition)),
            map((treffer) => raetselgruppenActions.raetselgruppen_found({ treffer }))
        );
    });

    selectRaetselgruppe$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselgruppenActions.select_raetselgruppe),
            concatMap((action) => this.#raetselgruppenHttpService.findById(action.raetselgruppe.id)),
            map((raetselgruppeDetails) => raetselgruppenActions.raetselgruppedetails_loaded({ raetselgruppeDetails: raetselgruppeDetails, navigateTo: 'raetselgruppen/details' }))
        )

    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.raetselgruppedetails_loaded),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    unselectRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.unselect_raetselgruppe),
            tap(() => {
                this.#router.navigateByUrl('raetselgruppen');
            }),
        ), { dispatch: false });
}