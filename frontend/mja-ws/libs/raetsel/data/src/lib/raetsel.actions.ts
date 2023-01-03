import { PageDefinition } from '@mja-ws/core/model';
import { RaetselsucheTreffer, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'FIND_RAETSEL': props<{suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition}>(),
        'RAETSEL_FOUND': props<{treffer: RaetselsucheTreffer}>(),
        'RAETSELLISTE_CLEARED': emptyProps(),
    }
});
