import { PageDefinition } from '@mja-ws/core/model';
import { RaetselgruppenTreffer, RaetselgruppenSuchparameter } from '@mja-ws/raetselgruppen/model';
import { createAction, createActionGroup, emptyProps, props } from '@ngrx/store';


export const raetselgruppenActions = createActionGroup({
    source: 'raetselgruppen',
    events: {
        'FIND_RAETSELGRUPPEN': props<{raetselgruppenSuchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition}>(),
        'RAETSELGRUPPEN_FOUND': props<{treffer: RaetselgruppenTreffer}>()
    }
});