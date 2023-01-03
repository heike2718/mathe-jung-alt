import { inject, Injectable } from "@angular/core";
import { noopAction } from "@mja-ws/shared/ngrx-utils";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { map, tap } from "rxjs";
import { raetselActions } from "./raetsel.actions";

@Injectable({
    providedIn: 'root'
})
export class RaetselEffects {

    #actions = inject(Actions);

    raetsellisteCleared$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.raetselliste_cleared),
            tap(() => console.log('raetsellisteCleared')),
            map(() => noopAction())
        );
    });

}