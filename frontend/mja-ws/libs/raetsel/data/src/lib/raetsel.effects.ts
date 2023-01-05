import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { noopAction } from "@mja-ws/shared/ngrx-utils";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { concatMap, map, tap } from "rxjs";
import { RaetselHttpService } from "./raetsel-http.service";
import { raetselActions } from "./raetsel.actions";

@Injectable({
    providedIn: 'root'
})
export class RaetselEffects {

    #actions = inject(Actions);
    #raetselHttpService = inject(RaetselHttpService);
    #router = inject(Router);

    findRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.find_raetsel),
            concatMap((action) => this.#raetselHttpService.findRaetsel(action.suchfilter, action.pageDefinition)),
            map((treffer) => raetselActions.raetsel_found({ treffer }))
        );
    });

    raetselSelected$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.raetsel_selected),
            concatMap((action) => this.#raetselHttpService.loadRaetselDetails(action.raetsel)),
            map((raetselDetails) => raetselActions.raetsel_details_loaded({ raetselDetails: raetselDetails, navigateTo: 'raetsel/details' }))
        );
    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.raetsel_details_loaded),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    raetsellisteCleared$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.raetselliste_cleared),
            tap(() => console.log('raetsellisteCleared')),
            map(() => noopAction())
        );
    });
}