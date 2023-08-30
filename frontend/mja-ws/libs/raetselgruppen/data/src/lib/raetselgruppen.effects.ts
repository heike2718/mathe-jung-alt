import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { GeneratedFile } from "@mja-ws/core/model";
import { FileDownloadService } from "@mja-ws/shared/util";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { concatMap, map, tap } from "rxjs";
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

    editRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.edit_raetselguppe),
            tap(() => {
                this.#router.navigateByUrl('raetselgruppen/edit');
            }),
        ), { dispatch: false });

    saveRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.save_raetselgruppe),
            concatMap((action) => this.#raetselgruppenHttpService.saveRaetselgruppe(action.editRaetselgruppePayload)),
            map((raetselgruppe) => raetselgruppenActions.raetselgruppe_saved({ raetselgruppe }))
        )
    );

    saveRaetselgruppenelement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.save_raetselgruppenelement),
            concatMap((action) => this.#raetselgruppenHttpService.saveRaetselgruppenelement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => raetselgruppenActions.raetselgruppenelemente_changed({ raetselgruppenDetails }))
        )
    );

    deleteRaetselgruppenelement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselgruppenActions.delete_raetselgruppenelement),
            concatMap((action) => this.#raetselgruppenHttpService.deleteRaetselgruppenelement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => raetselgruppenActions.raetselgruppenelemente_changed({ raetselgruppenDetails }))
        )
    );

    generiereVorschau$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.generiere_vorschau),
            concatMap((action) => this.#raetselgruppenHttpService.generiereVorschau(action.raetselgruppeID)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.file_generated({ pdf: genaratedFile }))
        )
    );



    generiereLaTeX$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.generiere_latex),
            concatMap((action) => this.#raetselgruppenHttpService.generiereLaTeX(action.raetselgruppeID)),
            map((genaratedFile: GeneratedFile) => raetselgruppenActions.file_generated({ pdf: genaratedFile }))
        )
    );

    downloadPDF$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselgruppenActions.file_generated),
            tap((action) => this.#downloadService.downloadPdf(action.pdf.fileData, action.pdf.fileName)),
        ), { dispatch: false });
}
