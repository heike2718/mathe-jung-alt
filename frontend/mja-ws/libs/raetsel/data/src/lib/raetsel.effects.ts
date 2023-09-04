import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { MessageService } from "@mja-ws/shared/messaging/api";
import { FileDownloadService } from "@mja-ws/shared/util";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { map, concatMap, tap, Observable, catchError, of } from "rxjs";
import { RaetselHttpService } from "./raetsel-http.service";
import { raetselActions } from "./raetsel.actions";
import { GeneratedFile, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE } from "@mja-ws/core/model";
import { safeConcatMap } from "@mja-ws/shared/ngrx-utils";

@Injectable({
    providedIn: 'root'
})
export class RaetselEffects {

    #actions = inject(Actions);
    #raetselHttpService = inject(RaetselHttpService);
    #router = inject(Router);
    #fileDownloadService = inject(FileDownloadService);
    #messageService = inject(MessageService);

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

    generateRaetselPng$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.generate_raetsel_png),
            concatMap(
                (action) => this.#raetselHttpService.generateRaetselPNGs(action.raetselID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)
                    .pipe(
                        map((generatedImages) => raetselActions.raetsel_png_generated({ images: generatedImages })),
                        catchError(() => of(raetselActions.latex_errors_detected()))
                    )
            ),
        );
    });

    generateRaetselPDF$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.generate_raetsel_pdf),
            concatMap(
                (action) => this.#raetselHttpService.generateRaetselPDF(action.raetselID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)
                    .pipe(
                        map((file) => raetselActions.raetsel_pdf_generated({ pdf: file })),
                        catchError(() => of(raetselActions.latex_errors_detected()))
                    )
            )
        );
    });

    downloadPDF$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.raetsel_pdf_generated),
            tap((action) => {
                this.#fileDownloadService.downloadPdf(action.pdf.fileData, action.pdf.fileName);
            }),
        ), { dispatch: false });


    findLatexLogs$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.find_latexlogs),
            concatMap((action) => this.#raetselHttpService.findLatexLogs(action.schluessel)),
            map((files) => raetselActions.latexlogs_found({ files: files }))
        ));

    latexLogsFound$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.latexlogs_found),
            tap((action) => {

                if (action.files.length > 0) {

                    action.files.forEach((file: GeneratedFile) => {
                        this.#fileDownloadService.downloadText(file.fileData, file.fileName);
                    })
                }
                else {
                    this.#messageService.warn('Logs sind nicht mehr vorhanden');
                }
            }),
        ), { dispatch: false });

    cancelSelectiont$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselActions.raetsel_cancel_selection),
            tap(() => this.#router.navigateByUrl('raetsel/uebersicht')),
        ), { dispatch: false });

    saveRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.save_raetsel),
            concatMap((action) => this.#raetselHttpService.saveRaetsel(action.editRaetselPayload)),
            map((raetselDetails) => raetselActions.raetsel_saved({ raetselDetails }))
        );
    });

    raetselSaved$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselActions.raetsel_saved),
            tap(() => this.#messageService.info('Rätsel erfolgreich gespeichert')),
        ), { dispatch: false });
}