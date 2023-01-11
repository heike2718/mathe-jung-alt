import { GeneratedFile, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition } from '@mja-ws/core/model';
import { EditRaetselPayload, Raetsel, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'FIND_RAETSEL': props<{suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition}>(),
        'RAETSEL_FOUND': props<{treffer: RaetselsucheTreffer}>(),
        'RAETSEL_SELECTED': props<{raetsel: Raetsel}>(),
        'RAETSEL_DETAILS_LOADED': props<{raetselDetails: RaetselDetails, navigateTo: string}>(),
        'GENERATE_RAETSEL_PNG': props<{ raetselID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>(),
        'RAETSEL_PNG_GENERATED': props<{ images: GeneratedImages }>(),
        'GENERATE_RAETSEL_PDF': props<{ raetselID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>(),
        'RAETSEL_PDF_GENERATED':props<{ pdf: GeneratedFile }>(),
        'RAETSELSUCHFILTER_CHANGED': props<{suchfilter: RaetselSuchfilter}>(),
        'RAETSEL_CANCEL_SELECTION': emptyProps(),
        'RESET_RAETSELSUCHFILTER': emptyProps(),
        'SAVE_RAETSEL': props<{editRaetselPayload: EditRaetselPayload}>(),
        'RAETSEL_SAVED': props<{raetselDetails: RaetselDetails}>(),
    }
});
