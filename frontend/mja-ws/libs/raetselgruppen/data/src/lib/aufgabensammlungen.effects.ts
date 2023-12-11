import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { GeneratedFile } from "@mja-ws/core/model";
import { FileDownloadService } from "@mja-ws/shared/util";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { switchMap, map, tap } from "rxjs";
import { AufgabensammlungenHttpService } from "./aufgabensammlungen-http.service";
import { aufgabensammlungenActions } from "./aufgabensammlungen.actions";
import { CoreFacade } from "@mja-ws/core/api";

@Injectable({ providedIn: 'root' })
export class AufgabensammlungenEffects {

    #actions = inject(Actions);
    #aufgabensammlungenHttpService = inject(AufgabensammlungenHttpService);
    #router = inject(Router);
    #downloadService = inject(FileDownloadService);
    #coreFacade = inject(CoreFacade);

    findRaetselgruppen$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(aufgabensammlungenActions.fIND_RAETSELGRUPPEN),
            switchMap((action) => this.#aufgabensammlungenHttpService.findAufgabensammlungen(action.aufgabensammlungenSuchparameter, action.pageDefinition)),
            map((treffer) => aufgabensammlungenActions.rAETSELGRUPPEN_FOUND({ treffer }))
        );
    });

    selectRaetselgruppe$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(aufgabensammlungenActions.sELECT_RAETSELGRUPPE),
            switchMap((action) => this.#aufgabensammlungenHttpService.findById(action.raetselgruppe.id)),
            map((aufgabensammlungDetails) => aufgabensammlungenActions.rAETSELGRUPPEDETAILS_LOADED({ aufgabensammlungDetails: aufgabensammlungDetails, navigateTo: 'aufgabensammlungen/details' }))
        )

    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.rAETSELGRUPPEDETAILS_LOADED),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    unselectRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.uNSELECT_RAETSELGRUPPE),
            tap(() => {
                this.#router.navigateByUrl('aufgabensammlungen');
            }),
        ), { dispatch: false });

    editRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.eDIT_RAETSELGUPPE),
            tap(() => {
                this.#router.navigateByUrl('aufgabensammlungen/edit');
            }),
        ), { dispatch: false });

    saveRaetselgruppe$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.sAVE_RAETSELGRUPPE),
            switchMap((action) => this.#aufgabensammlungenHttpService.saveAufgabensammlung(action.editAufgabensammlungPayload)),
            map((raetselgruppe) => aufgabensammlungenActions.rAETSELGRUPPE_SAVED({ raetselgruppe }))
        )
    );

    saveAufgabensammlungselement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.sAVE_RAETSELGRUPPENELEMENT),
            switchMap((action) => this.#aufgabensammlungenHttpService.saveAufgabensammlungselement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => aufgabensammlungenActions.rAETSELGRUPPENELEMENTE_CHANGED({ raetselgruppenDetails }))
        )
    );

    deleteAufgabensammlungselement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.dELETE_RAETSELGRUPPENELEMENT),
            switchMap((action) => this.#aufgabensammlungenHttpService.deleteAufgabensammlungselement(action.raetselgruppeID, action.payload)),
            map((raetselgruppenDetails) => aufgabensammlungenActions.rAETSELGRUPPENELEMENTE_CHANGED({ raetselgruppenDetails }))
        )
    );

    generiereArbeitsblatt$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_ARBEITSBLATT),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereArbeitsblattMitLoesungen(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereKnobelkartei$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_KNOBELKARTEI),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereKnobelkartei(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereVorschau$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_VORSCHAU),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereVorschau(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereLaTeX$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_LATEX),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereLaTeX(action.raetselgruppeID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map(({ data, fileName }) => aufgabensammlungenActions.bLOB_GENERATED({ data, fileName }))
        )
    );

    downloadPDF$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.fILE_GENERATED),
            tap((action) => this.#downloadService.downloadPdf(action.pdf.fileData, action.pdf.fileName)),
        ), { dispatch: false });

    downloadBlob$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.bLOB_GENERATED),
            tap((action) => this.#downloadService.downloadZip(action.data, action.fileName)),
        ), { dispatch: false });

    aufgabensammlungselementSelected$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.sELECT_RAETSELGRUPPENELEMENT),
            switchMap((action) => this.#coreFacade.loadRaetselPNGs(action.aufgabensammlungselement.raetselSchluessel)),
            map((images) => aufgabensammlungenActions.eLEMENT_IMAGES_LOADED({ generatedImages: images }))
        )
    );
}
