import { createActionGroup, emptyProps, props } from '@ngrx/store';
import {  CreateEmbeddableImageRequestDto, EmbeddableImageInfo, EmbeddableImageResponseDto, EmbeddableImageVorschau, ReplaceEmbeddableImageRequestDto } from '@mja-ws/embeddable-images/model';

export const embeddableImagesActions = createActionGroup({
    source: 'EmbeddableImages',
    events: {
        'LADE_VORSCHAU': props<{pfad: string}>(),
        'IMAGE_INFO_SELECTED': props<{embeddableImageInfo: EmbeddableImageInfo}>(),
        'VORSCHAU_GELADEN': props<{embeddableImageVorschau: EmbeddableImageVorschau}>(),
        'CLEAR_VORSCHAU': emptyProps(),
        'CREATE_EMBEDDABLE_IMAGE': props<{requestDto: CreateEmbeddableImageRequestDto}>(),
        'EMBEDDABLE_IMAGE_CREATED': props<{ responseDto: EmbeddableImageResponseDto }>(),
        'REPLACE_EMEDDABLE_IMAGE': props<{requestDto: ReplaceEmbeddableImageRequestDto}>(),
        'EMBEDABBLE_IMAGE_REPLACED': props<{ responseDto: EmbeddableImageResponseDto}>(),
        'RESET_STATE': emptyProps
    }
});
