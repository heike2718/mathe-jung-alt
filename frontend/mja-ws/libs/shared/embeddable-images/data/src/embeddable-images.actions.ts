import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { CreateEmbeddableImageRequestDto, EmbeddableImageResponseDto, ReplaceEmbeddableImageRequestDto } from '@mja-ws/embeddable-images/model';
import { Message } from '@mja-ws/shared/messaging/api';

export const embeddableImagesActions = createActionGroup({
    source: 'EmbeddableImages',
    events: {
        'CREATE_EMBEDDABLE_IMAGE': props<{requestDto: CreateEmbeddableImageRequestDto}>(),
        'EMBEDDABLE_IMAGE_CREATED': props<{ responseDto: EmbeddableImageResponseDto }>(),
        'REPLACE_EMEDDABLE_IMAGE': props<{requestDto: ReplaceEmbeddableImageRequestDto}>(),
        'EMBEDABBLE_IMAGE_REPLACED': props<{message: Message}>(),
        'RESET_STATE': emptyProps
    }
});
