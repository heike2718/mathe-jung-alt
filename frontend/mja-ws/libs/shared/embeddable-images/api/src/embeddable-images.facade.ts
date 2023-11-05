import { Injectable, inject } from "@angular/core";
import { CreateEmbeddableImageRequestDto, EmbeddableImageContext, EmbeddableImageResponseDto, EmbeddableImageVorschau } from "@mja-ws/embeddable-images/model";
import { Store } from "@ngrx/store";
import { embeddableImagesActions, fromEmbeddableImages }  from '@mja-ws/embeddable-images/data';
import { Observable } from "rxjs";
import { filterDefined } from "@mja-ws/shared/ngrx-utils";
import { UploadedFile } from "@mja-ws/core/model";


@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesFacade {

    #store = inject(Store);

    embeddableImageVorschauGeladen$: Observable<boolean> = this.#store.select(fromEmbeddableImages.embeddableImageVorschauGeladen);
    selectedEmbeddableImageVorschau$: Observable<EmbeddableImageVorschau> = filterDefined(this.#store.select(fromEmbeddableImages.selectedEmbeddableImageVorschau));
    pfad$: Observable<string> = filterDefined(this.#store.select(fromEmbeddableImages.embeddableImageVorschauPfad));

    embeddableImageResponse$: Observable<EmbeddableImageResponseDto> = filterDefined(this.#store.select(fromEmbeddableImages.embeddableImagesResponse));

    vorschauLaden(relativerPfad: string): void {
        this.#store.dispatch(embeddableImagesActions.lADE_VORSCHAU({pfad: relativerPfad}));
    }
    
    createEmbeddableImage(context: EmbeddableImageContext, file: UploadedFile): void {
        
        this.#store.dispatch(embeddableImagesActions.cREATE_EMBEDDABLE_IMAGE({requestDto: {
            context: context,
            file: file
        }}));
    }

    clearVorschau(): void {
        this.#store.dispatch(embeddableImagesActions.cLEAR_VORSCHAU());
    }

    replaceEmbeddableImage(pfad: string, context: EmbeddableImageContext, file: UploadedFile): void {
        
        this.#store.dispatch(embeddableImagesActions.rEPLACE_EMEDDABLE_IMAGE({requestDto: {
            context: context,
            relativerPfad: pfad,
            file: file
        }}));
    }

    resetState(): void {

        this.#store.dispatch(embeddableImagesActions.rESET_STATE());
    }

}