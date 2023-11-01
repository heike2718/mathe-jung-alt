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

    createEmbeddableImage$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(embeddableImagesActions.cREATE_EMBEDDABLE_IMAGE),
            concatMap((action) => this.#httpService.createEmbeddableImage(action.requestDto)),
            map((responseDto) => embeddableImagesActions.eMBEDDABLE_IMAGE_CREATED({ responseDto }))
        );
    });
    

}