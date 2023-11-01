
import { createFeature, createReducer, on } from "@ngrx/store";
import { embeddableImagesActions } from './embeddable-images.actions';
import { EmbeddableImageResponseDto, initialEmbeddableImageResponseDto } from "@mja-ws/embeddable-images/model";
import { Message } from "@mja-ws/shared/messaging/api";

export interface EmbeddableImagesState {
    embeddableImagesResponse: EmbeddableImageResponseDto | undefined;
    replaceEmbeddableImageMessage: Message | undefined;
}

const initialState: EmbeddableImagesState = {
    embeddableImagesResponse: initialEmbeddableImageResponseDto,
    replaceEmbeddableImageMessage: undefined
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
                    replaceEmbeddableImageMessage: action.message
                }
            )
        ),
        on(
            embeddableImagesActions.rESET_STATE,
            (_state, _action) => (initialState)
        ),
    )
});

