import { QuelleUI } from '@mja-ws/core/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const coreQuelleActions = createActionGroup({
    source: 'CoreQuelle',
    events: {
        'LOAD_QUELLE_ADMIN': emptyProps(),
        'CORE_QUELLE_ADMIN_LOADED': props<{ quelle: QuelleUI | undefined }>(),
        'CORE_QUELLE_ADMIN_REMOVE':  emptyProps()
    }
});
