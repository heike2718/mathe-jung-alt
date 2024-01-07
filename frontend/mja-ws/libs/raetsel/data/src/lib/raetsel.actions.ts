import { FontName, GeneratedFile, GeneratedImages, Herkunftstyp, LaTeXLayoutAntwortvorschlaege, Medienart, PageDefinition, QuelleDto, Schriftgroesse } from '@mja-ws/core/model';
import { EditRaetselPayload, GUIEditRaetselPayload, LinkedAufgabensammlung, MediumQuelleDto, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'EMBEDDED_IMAGES_FOUND': props<{ files: GeneratedFile[] }>(),
        'INIT_EDIT_RAETSEL_PAYLOD': props<{ payload: GUIEditRaetselPayload }>(),
        'FIND_LINKED_AUFGABENSAMMLUNGEN': props<{ raetselId: string }>(),
        'LINKED_AUFGABENSAMMLUNGEN_FOUND': props<{ linkedAufgabensammlungen: LinkedAufgabensammlung[] }>(),
        'FIND_MEDIEN_FOR_QUELLE': props<{ medienart: Medienart }>(),
        'MEDIEN_FOR_QUELLE_FOUND': props<{ result: MediumQuelleDto[] }>(),
        'FIND_EMBEDDED_IMAGES': props<{ raetselID: string }>(),
        'FIND_LATEXLOGS': props<{ schluessel: string }>(),
        'FIND_RAETSEL': props<{ admin: boolean, suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition }>(),
        'FIND_RAETSEL_LATEX': props<{ raetselID: string }>(),
        'GENERATE_RAETSEL_PNG': props<{ raetselID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege }>(),
        'GENERATE_RAETSEL_PDF': props<{ raetselID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege }>(),
        'LATEX_ERRORS_DETECTED': emptyProps(),
        'LATEXLOGS_FOUND': props<{ files: GeneratedFile[] }>(),
        'RAETSEL_DETAILS_LOADED': props<{ raetselDetails: RaetselDetails, navigateTo: string }>(),
        'RAETSEL_FOUND': props<{ treffer: RaetselsucheTreffer }>(),
        'RAETSEL_LATEX_FOUND': props<{ files: GeneratedFile[] }>(),
        'RAETSEL_PNG_GENERATED': props<{ images: GeneratedImages }>(),
        'RAETSEL_PDF_GENERATED': props<{ pdf: GeneratedFile }>(),
        'RAETSEL_SAVED': props<{ raetselDetails: RaetselDetails }>(),
        'RAETSEL_SELECT_PAGE': props<{ pageDefinition: PageDefinition }>(),
        'RAETSEL_SELECTED': props<{ schluessel: string }>(),
        'RAETSELSUCHFILTER_CHANGED': props<{ suchfilter: RaetselSuchfilter }>(),
        'RAETSEL_CANCEL_SELECTION': emptyProps(),
        'RESET_RAETSELSUCHFILTER': emptyProps(),
        'SAVE_RAETSEL': props<{ editRaetselPayload: EditRaetselPayload }>(),
    }
});
