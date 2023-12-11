import { GeneratedImages, initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungTrefferItem } from "@mja-ws/raetselgruppen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { aufgabensammlungenActions } from "./aufgabensammlungen.actions";

export interface RaetselgruppenState {
    readonly loaded: boolean;
    readonly anzahlTrefferGesamt: number;
    readonly page: AufgabensammlungTrefferItem[];
    readonly paginationState: PaginationState;
    readonly aufgabensammlungBasisdaten: AufgabensammlungBasisdaten| undefined;
    readonly aufgabensammlungDetails: AufgabensammlungDetails | undefined;
    readonly selectedAufgabensammlungselement: Aufgabensammlungselement | undefined;
    readonly selectedElementImages: GeneratedImages | undefined;
};

const initialRaetselgruppenState: RaetselgruppenState = {
    loaded: false,
    anzahlTrefferGesamt: 0,
    page: [],
    paginationState: initialPaginationState,
    aufgabensammlungBasisdaten: undefined,
    aufgabensammlungDetails: undefined,
    selectedAufgabensammlungselement: undefined,
    selectedElementImages: undefined
};

export const aufgabensammlungenFeature = createFeature({
    name: 'raetselgruppen',
    reducer: createReducer(
        initialRaetselgruppenState,
        on(aufgabensammlungenActions.rAETSELGRUPPEN_FOUND, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                page: action.treffer.items,
                loaded: true,
                selectedAufgabensammlungselement: undefined,
                selectedElementImages: undefined
            };
        }),
        on((aufgabensammlungenActions.rAETSELGRUPPEN_SELECT_PAGE), (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState,
                    pageDefinition: {
                        pageIndex: action.pageDefinition.pageIndex,
                        pageSize: action.pageDefinition.pageSize,
                        sortDirection: action.pageDefinition.sortDirection 
                    } 
                }
            };
        }),
        on(aufgabensammlungenActions.eDIT_RAETSELGUPPE, (state, action) => {
            return {
                ...state,
                aufgabensammlungBasisdaten: action.aufgabensammlungBasisdaten
            }
        }),
        on(aufgabensammlungenActions.rAETSELGRUPPE_SAVED, (state, action) => {
            const aufgabensammlungBasisdaten: AufgabensammlungBasisdaten = action.raetselgruppe;
            return { ...state, aufgabensammlungBasisdaten: aufgabensammlungBasisdaten };
        }),
        on(aufgabensammlungenActions.rAETSELGRUPPEDETAILS_LOADED, (state, action) => {
            return {...state, aufgabensammlungDetails: action.aufgabensammlungDetails, aufgabensammlungBasisdaten: undefined};
        }),
        on(aufgabensammlungenActions.uNSELECT_RAETSELGRUPPE, (state, _action) => {
            return {...state, aufgabensammlungDetails: undefined, aufgabensammlungBasisdaten: undefined, selectedAufgabensammlungselement: undefined, selectedElementImages: undefined}
        }),
        on(aufgabensammlungenActions.rAETSELGRUPPENELEMENTE_CHANGED, (state, action) => ({ ...state, aufgabensammlungDetails: action.raetselgruppenDetails })),
        on(aufgabensammlungenActions.sELECT_RAETSELGRUPPENELEMENT, (state, action) => ({...state, selectedAufgabensammlungselement: action.aufgabensammlungselement})),
        on(aufgabensammlungenActions.eLEMENT_IMAGES_LOADED, (state, action) => ({...state, selectedElementImages: action.generatedImages}))
    )
});