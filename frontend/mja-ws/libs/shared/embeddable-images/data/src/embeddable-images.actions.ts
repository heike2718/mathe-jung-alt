import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { CreateEmbeddableImageRequestDto, EmbeddableImageResponseDto } from '@mja-ws/embeddable-images/model';

export const embeddableImagesActions = createActionGroup({
    source: 'EmbeddableImages',
    events: {
        'CREATE_EMBEDDABLE_IMAGE': props<{requestDto: CreateEmbeddableImageRequestDto}>(),
        'EMBEDDABLE_IMAGE_CREATED': props<{ responseDto: EmbeddableImageResponseDto }>(),
        'RESET_STATE': emptyProps
    }
});
