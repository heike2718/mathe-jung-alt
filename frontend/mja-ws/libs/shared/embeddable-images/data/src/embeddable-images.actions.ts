import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { CreateEmbeddableImageRequestDto, EmbeddableImageResponseDto, EmbeddableImageVorschau, ReplaceEmbeddableImageRequestDto } from '@mja-ws/embeddable-images/model';
import { Message } from '@mja-ws/shared/messaging/api';

export const embeddableImagesActions = createActionGroup({
    source: 'EmbeddableImages',
    events: {
        'LADE_VORSCHAU': props<{pfad: string}>(),
        'VORSCHAU_GELADEN': props<{embeddableImageVorschau: EmbeddableImageVorschau}>(),
        'CLEAR_VORSCHAU': emptyProps(),
        'CREATE_EMBEDDABLE_IMAGE': props<{requestDto: CreateEmbeddableImageRequestDto}>(),
        'EMBEDDABLE_IMAGE_CREATED': props<{ responseDto: EmbeddableImageResponseDto }>(),
        'REPLACE_EMEDDABLE_IMAGE': props<{requestDto: ReplaceEmbeddableImageRequestDto}>(),
        'EMBEDABBLE_IMAGE_REPLACED': props<{message: Message}>(),
        'RESET_STATE': emptyProps
    }
});
