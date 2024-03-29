import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { MessageService } from "@mja-ws/shared/messaging/api";
import { FileDownloadService } from "@mja-ws/shared/util";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { map, switchMap, tap, catchError, of } from "rxjs";
import { RaetselHttpService } from "./raetsel-http.service";
import { raetselActions } from "./raetsel.actions";
import { GeneratedFile } from "@mja-ws/core/model";
import { CoreFacade } from "@mja-ws/core/api";

@Injectable({
    providedIn: 'root'
})
export class RaetselEffects {

    #actions = inject(Actions);
    #raetselHttpService = inject(RaetselHttpService);
    #router = inject(Router);
    #fileDownloadService = inject(FileDownloadService);
    #messageService = inject(MessageService);
    #coreFacade = inject(CoreFacade);

    findRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.fIND_RAETSEL),
            switchMap((action) => this.#raetselHttpService.findRaetsel(action.admin, action.suchfilter, action.pageDefinition)),
            map((treffer) => raetselActions.rAETSEL_FOUND({ treffer }))
        );
    });

    raetselSelected$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.rAETSEL_SELECTED),
            switchMap((action) => this.#raetselHttpService.loadRaetselDetails(action.schluessel)),
            map((raetselDetails) => raetselActions.rAETSEL_DETAILS_LOADED({ raetselDetails: raetselDetails, navigateTo: 'raetsel/details' }))
        );
    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.rAETSEL_DETAILS_LOADED),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    generateRaetselPng$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.gENERATE_RAETSEL_PNG),
            switchMap(
                (action) => this.#raetselHttpService.generateRaetselPNGs(action.raetselID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)
                    .pipe(
                        map((generatedImages) => raetselActions.rAETSEL_PNG_GENERATED({ images: generatedImages })),
                        catchError(() => of(raetselActions.lATEX_ERRORS_DETECTED()))
                    )
            ),
        );
    });

    generateRaetselPDF$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.gENERATE_RAETSEL_PDF),
            switchMap(
                (action) => this.#raetselHttpService.generateRaetselPDF(action.raetselID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)
                    .pipe(
                        map((file) => raetselActions.rAETSEL_PDF_GENERATED({ pdf: file })),
                        catchError(() => of(raetselActions.lATEX_ERRORS_DETECTED()))
                    )
            )
        );
    });

    downloadPDF$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.rAETSEL_PDF_GENERATED),
            tap((action) => {
                this.#fileDownloadService.downloadPdf(action.pdf.fileData, action.pdf.fileName);
            }),
        ), { dispatch: false });


    findLatexLogs$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.fIND_LATEXLOGS),
            switchMap((action) => this.#raetselHttpService.downloadLatexLogs(action.schluessel)),
            map((files) => raetselActions.lATEXLOGS_FOUND({ files: files }))
        ));

    latexLogsFound$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.lATEXLOGS_FOUND),
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

    findEmbeddedImages$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.fIND_EMBEDDED_IMAGES),
            switchMap((action) => this.#raetselHttpService.downloadEmbeddedImages(action.raetselID)),
            map((files) => raetselActions.eMBEDDED_IMAGES_FOUND({ files: files }))
        ));

    embeddedImagesFound$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.eMBEDDED_IMAGES_FOUND),
            tap((action) => {

                if (action.files.length > 0) {

                    action.files.forEach((file: GeneratedFile) => {
                        this.#fileDownloadService.downloadText(file.fileData, file.fileName);
                    })
                }
                else {
                    this.#messageService.warn('keine der referenzierten Grafik-Dateien existiert');
                }
            }),
        ), { dispatch: false });

    findRaetselLaTeX$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.fIND_RAETSEL_LATEX),
            switchMap((action) => this.#raetselHttpService.downloadRaetselLatex(action.raetselID)),
            map((files) => raetselActions.eMBEDDED_IMAGES_FOUND({ files: files }))
        ));

    raetselLaTeXFound$ = createEffect(() =>

        this.#actions.pipe(
            ofType(raetselActions.rAETSEL_LATEX_FOUND),
            tap((action) => {

                if (action.files.length > 0) {

                    action.files.forEach((file: GeneratedFile) => {
                        this.#fileDownloadService.downloadText(file.fileData, file.fileName);
                    })
                }
                else {
                    this.#messageService.warn('Das Rätsel hat keine Frage und keine Lösung. Spiel am besten Lotto.');
                }
            }),
        ), { dispatch: false });

    cancelSelectiont$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselActions.rAETSEL_CANCEL_SELECTION),
            tap(() => this.#router.navigateByUrl('raetsel/uebersicht')),
        ), { dispatch: false });

    saveRaetsel$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.sAVE_RAETSEL),
            switchMap((action) => this.#raetselHttpService.saveRaetsel(action.editRaetselPayload)),
            map((raetselDetails) => raetselActions.rAETSEL_SAVED({ raetselDetails }))
        );
    });

    raetselSaved$ = createEffect(() =>
        this.#actions.pipe(
            ofType(raetselActions.rAETSEL_SAVED),
            tap(() => {                
                this.#messageService.info('Rätsel erfolgreich gespeichert');
            }),
        ), { dispatch: false });

    findMedienForQuelle$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(raetselActions.fIND_MEDIEN_FOR_QUELLE),
            switchMap((action) => this.#raetselHttpService.findByMedienart(action.medienart)),
            map((result) => raetselActions.mEDIEN_FOR_QUELLE_FOUND({ result }))
        );
    });

    findLinkedAufgabensammlungen$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(raetselActions.fIND_LINKED_AUFGABENSAMMLUNGEN),
            switchMap((action) => this.#raetselHttpService.findLinkedAufgabensammlungen(action.raetselId)),
            map((linkedAufgabensammlungen) => raetselActions.lINKED_AUFGABENSAMMLUNGEN_FOUND({ linkedAufgabensammlungen }))
        );
    });
}