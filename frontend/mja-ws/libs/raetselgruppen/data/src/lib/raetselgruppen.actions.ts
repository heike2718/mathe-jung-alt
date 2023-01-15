import { GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition } from '@mja-ws/core/model';
import { RaetselgruppenTreffer, RaetselgruppenSuchparameter, RaetselgruppenTrefferItem, RaetselgruppeDetails } from '@mja-ws/raetselgruppen/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';


export const raetselgruppenActions = createActionGroup({
    source: 'raetselgruppen',
    events: {
        'FIND_RAETSELGRUPPEN': props<{raetselgruppenSuchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition}>(),
        'RAETSELGRUPPEN_FOUND': props<{treffer: RaetselgruppenTreffer}>(),
        'RAETSELGRUPPEN_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'SELECT_RAETSELGRUPPE': props<{ raetselgruppe: RaetselgruppenTrefferItem }>(),
        'UNSELECT_RAETSELGRUPPE': emptyProps(),
        'RAETSELGRUPPEDETAILS_LOADED': props<{raetselgruppeDetails: RaetselgruppeDetails, navigateTo: string}>(),
        'GENERIERE_VORSCHAU': props<{raetselgruppeID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>(),
        'VORSCHAU_GENERATED': props<{ pdf: GeneratedFile }>(),
        'GENERIERE_LATEX': props<{raetselgruppeID: string}>(),
        'LATEX_GENERATED': props<{ tex: GeneratedFile }>()
    }
});