import { FONT_NAME, GeneratedFile, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, SCHRIFTGROESSE } from '@mja-ws/core/model';
import { EditRaetselPayload, Raetsel, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'EMBEDDED_IMAGES_FOUND':props<{ files: GeneratedFile[] }>(),
        'FIND_EMBEDDED_IMAGES': props<{raetselID: string}>(),
        'FIND_LATEXLOGS': props<{schluessel: string}>(),
        'FIND_RAETSEL': props<{admin: boolean, suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition}>(),
        'FIND_RAETSEL_LATEX': props<{raetselID: string}>(),
        'FINISH_EDIT': emptyProps(),
        'GENERATE_RAETSEL_PNG': props<{ raetselID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>(),
        'GENERATE_RAETSEL_PDF': props<{ raetselID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>(),
        'LATEX_ERRORS_DETECTED': emptyProps(),
        'LATEXLOGS_FOUND':props<{ files: GeneratedFile[] }>(),
        'PREPARE_EDIT': emptyProps(),
        'RAETSEL_DETAILS_LOADED': props<{raetselDetails: RaetselDetails, navigateTo: string}>(),
        'RAETSEL_FOUND': props<{treffer: RaetselsucheTreffer}>(),
        'RAETSEL_LATEX_FOUND':props<{ files: GeneratedFile[] }>(),
        'RAETSEL_PNG_GENERATED': props<{ images: GeneratedImages }>(),
        'RAETSEL_PDF_GENERATED':props<{ pdf: GeneratedFile }>(),
        'RAETSEL_SAVED': props<{raetselDetails: RaetselDetails}>(),
        'RAETSEL_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'RAETSEL_SELECTED': props<{schluessel: string}>(),
        'RAETSELSUCHFILTER_CHANGED': props<{suchfilter: RaetselSuchfilter}>(),
        'RAETSEL_CANCEL_SELECTION': emptyProps(),
        'RESET_RAETSELSUCHFILTER': emptyProps(),
        'SAVE_RAETSEL': props<{editRaetselPayload: EditRaetselPayload}>(),       
        
    }
});
