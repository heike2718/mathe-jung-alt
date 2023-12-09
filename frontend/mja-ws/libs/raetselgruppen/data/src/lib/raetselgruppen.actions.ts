import { FONT_NAME, GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, SCHRIFTGROESSE, GeneratedImages } from '@mja-ws/core/model';
import { RaetselgruppenTreffer, RaetselgruppenSuchparameter, RaetselgruppenTrefferItem, RaetselgruppeDetails, RaetselgruppeBasisdaten, EditRaetselgruppePayload, EditRaetselgruppenelementPayload, Raetselgruppenelement } from '@mja-ws/raetselgruppen/model';
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
        'GENERIERE_ARBEITSBLATT': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_KNOBELKARTEI': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_VORSCHAU': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'FILE_GENERATED': props<{ pdf: GeneratedFile }>(),
        'BLOB_GENERATED': props<{ data: Blob, fileName: string }>(),
        'GENERIERE_LATEX': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'EDIT_RAETSELGUPPE': props<{raetselgruppeBasisdaten: RaetselgruppeBasisdaten}>(),
        'SAVE_RAETSELGRUPPE': props<{editRaetselgruppePayload: EditRaetselgruppePayload}>(),
        'RAETSELGRUPPE_SAVED': props<{raetselgruppe: RaetselgruppeBasisdaten}>(),
        'SELECT_RAETSELGRUPPENELEMENT': props<{raetselgruppenelement: Raetselgruppenelement}>(),
        'ELEMENT_IMAGES_LOADED': props<{generatedImages: GeneratedImages}>(),
        'UNSELECT_RAETSELGRUPPENELEMENT': emptyProps(),
        'SAVE_RAETSELGRUPPENELEMENT': props<{raetselgruppeID: string, payload: EditRaetselgruppenelementPayload}>(),
        'DELETE_RAETSELGRUPPENELEMENT': props<{raetselgruppeID: string, payload: Raetselgruppenelement}>(),
        'RAETSELGRUPPENELEMENTE_CHANGED': props<{raetselgruppenDetails: RaetselgruppeDetails}>()
    }
});