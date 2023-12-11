import { FONT_NAME, GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, SCHRIFTGROESSE, GeneratedImages } from '@mja-ws/core/model';
import { AufgabensammlungenTreffer, AufgabensammlungenSuchparameter, AufgabensammlungTrefferItem, AufgabensammlungDetails, AufgabensammlungBasisdaten, EditAufgabensammlungPayload, EditAufgabensammlungselementPayload, Aufgabensammlungselement } from '@mja-ws/raetselgruppen/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';


export const aufgabensammlungenActions = createActionGroup({
    source: 'raetselgruppen',
    events: {
        'FIND_RAETSELGRUPPEN': props<{aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition}>(),
        'RAETSELGRUPPEN_FOUND': props<{treffer: AufgabensammlungenTreffer}>(),
        'RAETSELGRUPPEN_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'SELECT_RAETSELGRUPPE': props<{ raetselgruppe: AufgabensammlungTrefferItem }>(),
        'UNSELECT_RAETSELGRUPPE': emptyProps(),
        'RAETSELGRUPPEDETAILS_LOADED': props<{aufgabensammlungDetails: AufgabensammlungDetails, navigateTo: string}>(),
        'GENERIERE_ARBEITSBLATT': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_KNOBELKARTEI': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_VORSCHAU': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'FILE_GENERATED': props<{ pdf: GeneratedFile }>(),
        'BLOB_GENERATED': props<{ data: Blob, fileName: string }>(),
        'GENERIERE_LATEX': props<{raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'EDIT_RAETSELGUPPE': props<{aufgabensammlungBasisdaten: AufgabensammlungBasisdaten}>(),
        'SAVE_RAETSELGRUPPE': props<{editAufgabensammlungPayload: EditAufgabensammlungPayload}>(),
        'RAETSELGRUPPE_SAVED': props<{raetselgruppe: AufgabensammlungBasisdaten}>(),
        'SELECT_RAETSELGRUPPENELEMENT': props<{aufgabensammlungselement: Aufgabensammlungselement}>(),
        'ELEMENT_IMAGES_LOADED': props<{generatedImages: GeneratedImages}>(),
        'UNSELECT_RAETSELGRUPPENELEMENT': emptyProps(),
        'SAVE_RAETSELGRUPPENELEMENT': props<{raetselgruppeID: string, payload: EditAufgabensammlungselementPayload}>(),
        'DELETE_RAETSELGRUPPENELEMENT': props<{raetselgruppeID: string, payload: Aufgabensammlungselement}>(),
        'RAETSELGRUPPENELEMENTE_CHANGED': props<{raetselgruppenDetails: AufgabensammlungDetails}>()
    }
});