import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImagesHttpService } from "./embeddable-images-http.service";
import { map, switchMap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesEffects {

    #actions = inject(Actions);
    #httpService = inject(EmbeddableImagesHttpService);

    ladeVorschau$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.lADE_VORSCHAU),
            switchMap((action) => this.#httpService.loadGrafik(action.pfad)),
            map((embeddableImageVorschau) => embeddableImagesActions.vORSCHAU_GELADEN({embeddableImageVorschau }))
        );
    });

    createEmbeddableImage$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.cREATE_EMBEDDABLE_IMAGE),
            switchMap((action) => this.#httpService.createEmbeddableImage(action.requestDto)),
            map((responseDto) => embeddableImagesActions.eMBEDDABLE_IMAGE_CREATED({ responseDto }))
        );
    });

    replaceEmbeddableImage$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.rEPLACE_EMEDDABLE_IMAGE),
            switchMap((action) => this.#httpService.replaceEmbeddableImage(action.requestDto)),
            map((responseDto) => embeddableImagesActions.eMBEDABBLE_IMAGE_REPLACED({ responseDto }))
        );
    });
}