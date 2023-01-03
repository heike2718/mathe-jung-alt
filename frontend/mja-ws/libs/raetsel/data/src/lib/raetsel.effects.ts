import { inject, Injectable } from "@angular/core";
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

    findRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.find_raetsel),
            concatMap((action) => this.#raetselHttpService.findRaetsel(action.suchfilter, action.pageDefinition)),
            map((treffer) => raetselActions.raetsel_found({ treffer }))
        )
    });

    raetsellisteCleared$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.raetselliste_cleared),
            tap(() => console.log('raetsellisteCleared')),
            map(() => noopAction())
        );
    });

}