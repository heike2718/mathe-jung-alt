import { PageDefinition } from '@mja-ws/core/model';
import { Raetsel, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'FIND_RAETSEL': props<{suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition}>(),
        'RAETSEL_FOUND': props<{treffer: RaetselsucheTreffer}>(),
        'RAETSEL_SELECTED': props<{raetsel: Raetsel}>(),
        'RAETSEL_DETAILS_LOADED': props<{raetselDetails: RaetselDetails}>(),
        'RAETSELLISTE_CLEARED': emptyProps(),
    }
});
