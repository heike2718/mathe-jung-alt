import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MedienHttpService } from "./medien-http.service";
import { medienActions } from "./medien.actions";
import { map, switchMap } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class MedienEffects {

    #actions = inject(Actions);
    #medienHttpService = inject(MedienHttpService);

    findMedien$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(medienActions.fIND_MEDIEN),
            switchMap((action) => this.#medienHttpService.findMedien(action.suchstring, action.pageDefinition)),
            map((result) => medienActions.mEDIEN_FOUND({ result }))
        );
    });

}