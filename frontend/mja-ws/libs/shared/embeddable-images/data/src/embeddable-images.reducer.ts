
import { createFeature, createReducer, on } from "@ngrx/store";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImageResponseDto, initialEmbeddableImageResponseDto } from "@mja-ws/embeddable-images/model";

export interface EmbeddableImagesState {
    embeddableImagesResponse: EmbeddableImageResponseDto | undefined;
}

const initialState: EmbeddableImagesState = {
    embeddableImagesResponse: initialEmbeddableImageResponseDto
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
            embeddableImagesActions.rESET_STATE,
            (_state, _action) => (initialState)
        ),
    )
});

