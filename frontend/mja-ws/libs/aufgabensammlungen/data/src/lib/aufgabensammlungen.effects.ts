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

    findAufgabensammlungen$ = createEffect(() => {
        return this.#actions.pipe(
            ofType(aufgabensammlungenActions.fIND_AUFGABENSAMMLUNGEN),
            switchMap((action) => this.#aufgabensammlungenHttpService.findAufgabensammlungen(action.aufgabensammlungenSuchparameter, action.pageDefinition)),
            map((treffer) => aufgabensammlungenActions.aUFGABENSAMMLUNGEN_FOUND({ treffer }))
        );
    });

    selectAufgabensammlung$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNG),
            switchMap((action) => this.#aufgabensammlungenHttpService.findById(action.aufgabensammlungId)),
            map((aufgabensammlungDetails) => aufgabensammlungenActions.aUFGABENSAMMLUNGDETAILS_LOADED({ aufgabensammlungDetails: aufgabensammlungDetails, navigateTo: 'aufgabensammlungen/details' }))
        )

    });

    showDetails$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.aUFGABENSAMMLUNGDETAILS_LOADED),
            tap((action) => {
                this.#router.navigateByUrl(action.navigateTo);
            }),
        ), { dispatch: false });

    unselectAufgabensammlung$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.uNSELECT_AUFGABENSAMMLUNG),
            tap(() => {
                this.#router.navigateByUrl('aufgabensammlungen');
            }),
        ), { dispatch: false });

    editAufgabensammlung$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.eDIT_AUFGABENSAMMLUNG),
            tap(() => {
                this.#router.navigateByUrl('aufgabensammlungen/edit');
            }),
        ), { dispatch: false });

    saveAufgabensammlung$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.sAVE_AUFGABENSAMMLUNG),
            switchMap((action) => this.#aufgabensammlungenHttpService.saveAufgabensammlung(action.editAufgabensammlungPayload)),
            map((aufgabensammlung) => aufgabensammlungenActions.aUFGABENSAMMLUNG_SAVED({ aufgabensammlung }))
        )
    );

    saveAufgabensammlungselement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.sAVE_AUFGABENSAMMLUNGSELEMENT),
            switchMap((action) => this.#aufgabensammlungenHttpService.saveAufgabensammlungselement(action.aufgabensammlungID, action.payload)),
            map((aufgabensammlungDetails) => aufgabensammlungenActions.aUFGABENSAMMLUNGSELEMENTE_CHANGED({ aufgabensammlungDetails }))
        )
    );

    deleteAufgabensammlungselement$ = createEffect(() =>

        this.#actions.pipe(
            ofType(aufgabensammlungenActions.dELETE_AUFGABENSAMMLUNGSELEMENT),
            switchMap((action) => this.#aufgabensammlungenHttpService.deleteAufgabensammlungselement(action.aufgabensammlungID, action.payload)),
            map((aufgabensammlungDetails) => aufgabensammlungenActions.aUFGABENSAMMLUNGSELEMENTE_CHANGED({ aufgabensammlungDetails }))
        )
    );

    generiereArbeitsblatt$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_ARBEITSBLATT),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereArbeitsblattMitLoesungen(action.aufgabensammlungID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereKnobelkartei$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_KNOBELKARTEI),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereKnobelkartei(action.aufgabensammlungID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereVorschau$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_VORSCHAU),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereVorschau(action.aufgabensammlungID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
            map((genaratedFile: GeneratedFile) => aufgabensammlungenActions.fILE_GENERATED({ pdf: genaratedFile }))
        )
    );

    generiereLaTeX$ = createEffect(() =>
        this.#actions.pipe(
            ofType(aufgabensammlungenActions.gENERIERE_LATEX),
            switchMap((action) => this.#aufgabensammlungenHttpService.generiereLaTeX(action.aufgabensammlungID, action.font, action.schriftgroesse, action.layoutAntwortvorschlaege)),
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
            ofType(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNGSELEMENT),
            switchMap((action) => this.#coreFacade.loadRaetselPNGs(action.aufgabensammlungselement.raetselSchluessel)),
            map((images) => aufgabensammlungenActions.eLEMENT_IMAGES_LOADED({ generatedImages: images }))
        )
    );
}
