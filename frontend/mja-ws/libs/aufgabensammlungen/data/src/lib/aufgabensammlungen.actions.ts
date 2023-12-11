import { FONT_NAME, GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, SCHRIFTGROESSE, GeneratedImages } from '@mja-ws/core/model';
import { AufgabensammlungenTreffer, AufgabensammlungenSuchparameter, AufgabensammlungTrefferItem, AufgabensammlungDetails, AufgabensammlungBasisdaten, EditAufgabensammlungPayload, EditAufgabensammlungselementPayload, Aufgabensammlungselement } from '@mja-ws/aufgabensammlungen/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';


export const aufgabensammlungenActions = createActionGroup({
    source: 'aufgabensammlungen',
    events: {
        'FIND_AUFGABENSAMMLUNGEN': props<{aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition}>(),
        'AUFGABENSAMMLUNGEN_FOUND': props<{treffer: AufgabensammlungenTreffer}>(),
        'AUFGABENSAMMLUNGEN_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
        'SELECT_AUFGABENSAMMLUNG': props<{ aufgabensammlung: AufgabensammlungTrefferItem }>(),
        'UNSELECT_AUFGABENSAMMLUNG': emptyProps(),
        'AUFGABENSAMMLUNGDETAILS_LOADED': props<{aufgabensammlungDetails: AufgabensammlungDetails, navigateTo: string}>(),
        'GENERIERE_ARBEITSBLATT': props<{aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_KNOBELKARTEI': props<{aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'GENERIERE_VORSCHAU': props<{aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'FILE_GENERATED': props<{ pdf: GeneratedFile }>(),
        'BLOB_GENERATED': props<{ data: Blob, fileName: string }>(),
        'GENERIERE_LATEX': props<{aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE}>(),
        'EDIT_AUFGABENSAMMLUNG': props<{aufgabensammlungBasisdaten: AufgabensammlungBasisdaten}>(),
        'SAVE_AUFGABENSAMMLUNG': props<{editAufgabensammlungPayload: EditAufgabensammlungPayload}>(),
        'AUFGABENSAMMLUNG_SAVED': props<{aufgabensammlung: AufgabensammlungBasisdaten}>(),
        'SELECT_AUFGABENSAMMLUNGSELEMENT': props<{aufgabensammlungselement: Aufgabensammlungselement}>(),
        'ELEMENT_IMAGES_LOADED': props<{generatedImages: GeneratedImages}>(),
        'SAVE_AUFGABENSAMMLUNGSELEMENT': props<{aufgabensammlungID: string, payload: EditAufgabensammlungselementPayload}>(),
        'DELETE_AUFGABENSAMMLUNGSELEMENT': props<{aufgabensammlungID: string, payload: Aufgabensammlungselement}>(),
        'AUFGABENSAMMLUNGSELEMENTE_CHANGED': props<{aufgabensammlungDetails: AufgabensammlungDetails}>()
    }
});