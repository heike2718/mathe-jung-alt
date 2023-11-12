import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { GeneratedFile } from "@mja-ws/core/model";
import { FileDownloadService } from "@mja-ws/shared/util";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { switchMap, map, tap } from "rxjs";
import { RaetselgruppenHttpService } from "./raetselgruppen-http.service";
import { raetselgruppenActions } from "./raetselgruppen.actions";

@Injectable({ providedIn: 'root' })
export class RaetselgruppenEffects {

    #actions = inject(Actions);
    #raetselgruppenHttpService = inject(RaetselgruppenHttpService);
    #router = inject(Router);
    #downloadService = inject(FileDownloadService);

    findRaetselgruppen$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(raetselgruppenActions.fIND_RAETSELGRUPPEN),
            switchMap((action) => this.#raetselgruppenHttpService.findRaetselgruppen(action.raetselgruppenSuchparameter, action.pageDefinition)),
            map((treffer) => raetselgruppenActions.rAETSELGRUPPEN_FOUND({ treffer }))
        );
    });

    selectRaetselgruppe$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselgruppenActions.sELECT_RAETSELGRUPPE),
            switchMap((action) => this.#raetselgruppenHttpService.findById(action.raetselgruppe.id)),
            map((raetselgruppeDetails) => raetselgruppenActions.rAETSELGRUPPEDETAILS_LOADED({ raetselgruppeDetails: raetselgruppeDetails, navigateTo: 'raetselgruppen/details' }))
        )

    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.rAETSELGRUPPEDETAILS_LOADED),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    unselectRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.uNSELECT_RAETSELGRUPPE),
            tap(() => {
                this.#router.navigateByUrl('raetselgruppen');
            }),
        ), { dispatch: false });

    editRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.eDIT_RAETSELGUPPE),
            tap(() => {
                this.#router.navigateByUrl('raetselgruppen/edit');
            }),
        ), { dispatch: false });

    saveRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.sAVE_RAETSELGRUPPE),
            switchMap((action) => this.#raetselgruppenHttpService.saveRaetselgruppe(action.editRaetselgruppePayload)),
            map((raetselgruppe) => raetselgruppenActions.rAETSELGRUPPE_SAVED({ raetselgruppe }))
        )
    );

    saveRaetselgruppenelement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.sAVE_RAETSELGRUPPENELEMENT),
            switchMap((action) => this.#raetselgruppenHttpService.saveRaetselgruppenelement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => raetselgruppenActions.rAETSELGRUPPENELEMENTE_CHANGED({ raetselgruppenDetails }))
        )
    );

    deleteRaetselgruppenelement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.dELETE_RAETSELGRUPPENELEMENT),
            switchMap((action) => this.#raetselgruppenHttpService.deleteRaetselgruppenelement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => raetselgruppenActions.rAETSELGRUPPENELEMENTE_CHANGED({ raetselgruppenDetails }))
        )
    );

    generiereArbeitsblatt$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.gENERIERE_ARBEITSBLATT),
            switchMap((action) => this.#raetselgruppenHttpService.generiereArbeitsblattMitLoesungen(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereKnobelkartei$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.gENERIERE_KNOBELKARTEI),
            switchMap((action) => this.#raetselgruppenHttpService.generiereKnobelkartei(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereVorschau$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.gENERIERE_VORSCHAU),
            switchMap((action) => this.#raetselgruppenHttpService.generiereVorschau(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );



    generiereLaTeX$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.gENERIERE_LATEX),
            switchMap((action) => this.#raetselgruppenHttpService.generiereLaTeX(action.raetselgruppeID, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    downloadPDF$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.fILE_GENERATED),
            tap((action) => this.#downloadService.downloadPdf(action.pdf.fileData, action.pdf.fileName)),
        ), { dispatch: false });
}
