import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MedienHttpService } from "./medien-http.service";
import { medienActions } from "./medien.actions";
import { map, switchMap, tap } from "rxjs";
import { Router } from "@angular/router";
import { MessageService } from "@mja-ws/shared/messaging/api";


@Injectable({
    providedIn: 'root'
})
export class MedienEffects {

    #actions = inject(Actions);
    #medienHttpService = inject(MedienHttpService);
    #router = inject(Router);
    #messageService = inject(MessageService);

    findMedien$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(medienActions.fIND_MEDIEN),
            switchMap((action) => this.#medienHttpService.findMedien(action.suchstring, action.pageDefinition)),
            map((result) => medienActions.mEDIEN_FOUND({ result }))
        );
    });

    unselectMedium$ = createEffect(() =>

        this.#actions.pipe(
            ofType(medienActions.uNSELECT_MEDIUM),
            tap(() => {
                this.#router.navigateByUrl('medien');
            }),
        ), { dispatch: false });

    saveMedum$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(medienActions.sAVE_MEDIUM),
            switchMap((action) => this.#medienHttpService.saveMedium(action.medium)),
            map((medium) => medienActions.mEDIUM_SAVED({ medium }))
        );
    });

    mediumSaved$ = createEffect(() =>
        this.#actions.pipe(
            ofType(medienActions.mEDIUM_SAVED),
            tap(() => {
                this.#messageService.info('Medium erfolgreich gespeichert');
            }),
        ), { dispatch: false });

}