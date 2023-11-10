
import { createFeature, createReducer, on } from "@ngrx/store";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImageResponseDto, EmbeddableImageVorschau, initialEmbeddableImageResponseDto } from "@mja-ws/embeddable-images/model";

export interface EmbeddableImagesState {
    readonly embeddableImagesResponse: EmbeddableImageResponseDto | undefined;
    readonly replaceEmbeddableImageMessage: string | undefined;
    readonly embeddableImageVorschauPfad: string | undefined;
    readonly embeddableImageVorschauGeladen: boolean;
    readonly selectedEmbeddableImageVorschau: EmbeddableImageVorschau | undefined;
}

const initialState: EmbeddableImagesState = {
    embeddableImagesResponse: initialEmbeddableImageResponseDto,
    replaceEmbeddableImageMessage: undefined,
    embeddableImageVorschauPfad: undefined,
    embeddableImageVorschauGeladen: false,
    selectedEmbeddableImageVorschau: undefined

};

export const embeddableImagesFeature = createFeature({
    name: 'embeddableImages',
    reducer: createReducer<EmbeddableImagesState>(
        initialState,
        on(
            embeddableImagesActions.eMBEDDABLE_IMAGE_CREATED,
            (state, action): EmbeddableImagesState => (
                {
                    ...state,
                    embeddableImagesResponse: action.responseDto
                }
            )
        ),
        on(
            embeddableImagesActions.eMBEDABBLE_IMAGE_REPLACED,
            (state, action): EmbeddableImagesState => (
                {
                    ...state,
                    replaceEmbeddableImageMessage: 'Grafikdatei erfolgreich ersetzt',
                    embeddableImagesResponse: action.responseDto
                }
            )
        ),
        on(embeddableImagesActions.lADE_VORSCHAU, (state, _action) => ({ ...state, selectedEmbeddableImageVorschau: undefined })),
        on(embeddableImagesActions.vORSCHAU_GELADEN, (state, action) => ({ ...state, embeddableImageVorschauGeladen: true, selectedEmbeddableImageVorschau: action.embeddableImageVorschau })),
        on(embeddableImagesActions.cLEAR_VORSCHAU, (state, _action) => ({ ...state, embeddableImageVorschauGeladen: false, selectedEmbeddableImageVorschau: undefined, embeddableImagesResponse: initialEmbeddableImageResponseDto })),
        on(
            embeddableImagesActions.rESET_STATE,
            (_state, _action) => (initialState)
        ),
    )
});

