import { HerkunftRaetsel } from '@mja-ws/core/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const coreQuelleActions = createActionGroup({
    source: 'CoreAutor',
    events: {
        'LOAD_AUTOR': emptyProps(),
        'CORE_AUTOR_LOADED': props<{ quelle: HerkunftRaetsel | undefined }>(),
        'REMOVE_AUTOR':  emptyProps()
    }
});
