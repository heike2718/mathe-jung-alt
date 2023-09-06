import {DeskriptorUI} from '@mja-ws/core/model';

import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const coreDeskriptorenActions = createActionGroup({
    source: 'CoreDeskriptor',
    events: {
        'LOAD_DESKRIPTOREN': emptyProps(),
        'CORE_DESKRIPTOREN_LOADED': props<{ deskriptoren: DeskriptorUI[] }>(),
        'CORE_DESKRIPTOREN_REMOVE':  emptyProps()
    }
});
