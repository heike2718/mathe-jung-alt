import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImagesHttpService } from "./embeddable-images-http.service";
import { concatMap, map } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesEffects {

    #actions = inject(Actions);
    #httpService = inject(EmbeddableImagesHttpService);

    ladeVorschau$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.lADE_VORSCHAU),
            concatMap((action) => this.#httpService.loadGrafik(action.pfad)),
            map((embeddableImageVorschau) => embeddableImagesActions.vORSCHAU_GELADEN({embeddableImageVorschau }))
        );
    });

    createEmbeddableImage$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.cREATE_EMBEDDABLE_IMAGE),
            concatMap((action) => this.#httpService.createEmbeddableImage(action.requestDto)),
            map((responseDto) => embeddableImagesActions.eMBEDDABLE_IMAGE_CREATED({ responseDto }))
        );
    });

    replaceEmbeddableImage$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.rEPLACE_EMEDDABLE_IMAGE),
            concatMap((action) => this.#httpService.replaceEmbeddableImage(action.requestDto)),
            map((message) => embeddableImagesActions.eMBEDABBLE_IMAGE_REPLACED({ message }))
        );
    });
    

}