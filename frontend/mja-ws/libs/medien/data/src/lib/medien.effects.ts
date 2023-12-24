import { Injectable, inject } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MedienHttpService } from "./medien-http.service";
import { medienActions } from "./medien.actions";
import { map, switchMap, tap } from "rxjs";
import { Router } from "@angular/router";
import { MessageService } from "@mja-ws/shared/messaging/api";
import { swallowEmptyArgument } from "@mja-ws/shared/util";


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

    editMedium$ = createEffect(() =>
        this.#actions.pipe(
            ofType(medienActions.eDIT_MEDIUM),
            tap(({ medium, nextUrl }) => {
                swallowEmptyArgument(medium, false);
                this.#router.navigateByUrl(nextUrl);
            }),
        ), { dispatch: false });

    selectMedium$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(medienActions.sELECT_MEDIUM),
            switchMap((action) => this.#medienHttpService.loadDetails(action.medium)),
            map((medium) => medienActions.mEDIUMDETAILS_LOADED({ details: medium, nextUrl: 'medien/details' }))
        );
    });

    mediumDetailsLoaded$ = createEffect(() =>
        this.#actions.pipe(
            ofType(medienActions.mEDIUMDETAILS_LOADED),
            tap(({ details, nextUrl }) => {
                swallowEmptyArgument(details, false);
                this.#router.navigateByUrl(nextUrl);
            }),
        ), { dispatch: false });

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