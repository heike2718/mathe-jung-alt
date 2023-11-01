import { Injectable, inject } from "@angular/core";
import { CreateEmbeddableImageRequestDto, EmbeddableImageContext, EmbeddableImageResponseDto, UploadedFile } from "@mja-ws/embeddable-images/model";
import { Store } from "@ngrx/store";
import { embeddableImagesActions, fromEmbeddableImages }  from '@mja-ws/embeddable-images/data';
import { Observable } from "rxjs";
import { filterDefined } from "@mja-ws/shared/ngrx-utils";


@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesFacade {

    #store = inject(Store);

    embeddableImageResponse$: Observable<EmbeddableImageResponseDto> = filterDefined(this.#store.select(fromEmbeddableImages.embeddableImagesResponse));

    public createEmbeddableImage(context: EmbeddableImageContext, file: UploadedFile): void {
        
        this.#store.dispatch(embeddableImagesActions.cREATE_EMBEDDABLE_IMAGE({requestDto: {
            context: context,
            file: file
        }}));
    }

    public resetState(): void {

        this.#store.dispatch(embeddableImagesActions.rESET_STATE());
    }

}