import { QuelleDto } from '@mja-ws/core/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const coreQuelleActions = createActionGroup({
    source: 'CoreAutor',
    events: {
        'LOAD_AUTOR': emptyProps(),
        'CORE_AUTOR_LOADED': props<{ quelle: QuelleDto | undefined }>(),
        'CORE_AUTOR_REPLACED': props<{ quelle: QuelleDto }>(),
        'REMOVE_AUTOR': emptyProps()
    }
});
