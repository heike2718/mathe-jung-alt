
import { createFeature, createReducer, on } from "@ngrx/store";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImageInfo, EmbeddableImageResponseDto, EmbeddableImageVorschau, initialEmbeddableImageResponseDto } from "@mja-ws/embeddable-images/model";
import { swallowEmptyArgument } from "@mja-ws/shared/util";

export interface EmbeddableImagesState {
    readonly embeddableImagesResponse: EmbeddableImageResponseDto | undefined;
    readonly replaceEmbeddableImageMessage: string | undefined;
    readonly embeddableImageVorschauPfad: string | undefined;
    readonly embeddableImageVorschauGeladen: boolean;
    readonly selectedEmbeddableImageInfo: EmbeddableImageInfo | undefined;
    readonly selectedEmbeddableImageVorschau: EmbeddableImageVorschau | undefined;
}

const initialState: EmbeddableImagesState = {
    embeddableImagesResponse: initialEmbeddableImageResponseDto,
    replaceEmbeddableImageMessage: undefined,
    embeddableImageVorschauPfad: undefined,
    embeddableImageVorschauGeladen: false,
    selectedEmbeddableImageInfo: undefined,
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
        on(embeddableImagesActions.iMAGE_INFO_SELECTED, (state, action) => ({ ...state, selectedEmbeddableImageInfo: action.embeddableImageInfo })),

        on(embeddableImagesActions.lADE_VORSCHAU, (state, action) => {

            swallowEmptyArgument(action, false);
            return {
                ...state, selectedEmbeddableImageVorschau: undefined
            }
        }),

        on(embeddableImagesActions.vORSCHAU_GELADEN, (state, action) =>
        ({
            ...state,
            embeddableImageVorschauGeladen: true,
            selectedEmbeddableImageVorschau: action.embeddableImageVorschau
        })),
        on(embeddableImagesActions.cLEAR_VORSCHAU, (state, action) => {

            swallowEmptyArgument(action, false);
            return {
                ...state,
                embeddableImageVorschauGeladen: false,
                selectedEmbeddableImageInfo: undefined,
                selectedEmbeddableImageVorschau: undefined,
                embeddableImagesResponse: initialEmbeddableImageResponseDto
            };
        }),
        on(
            embeddableImagesActions.rESET_STATE,
            () => (initialState)
        ),
    )
});

