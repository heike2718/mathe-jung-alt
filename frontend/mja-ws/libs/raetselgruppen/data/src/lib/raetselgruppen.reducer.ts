import { GeneratedImages, initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungTrefferItem } from "@mja-ws/raetselgruppen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselgruppenActions } from "./raetselgruppen.actions";

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

export const raetselgruppenFeature = createFeature({
    name: 'raetselgruppen',
    reducer: createReducer(
        initialRaetselgruppenState,
        on(raetselgruppenActions.rAETSELGRUPPEN_FOUND, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                page: action.treffer.items,
                loaded: true,
                selectedAufgabensammlungselement: undefined,
                selectedElementImages: undefined
            };
        }),
        on((raetselgruppenActions.rAETSELGRUPPEN_SELECT_PAGE), (state, action) => {
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
        on(raetselgruppenActions.eDIT_RAETSELGUPPE, (state, action) => {
            return {
                ...state,
                aufgabensammlungBasisdaten: action.aufgabensammlungBasisdaten
            }
        }),
        on(raetselgruppenActions.rAETSELGRUPPE_SAVED, (state, action) => {
            const aufgabensammlungBasisdaten: AufgabensammlungBasisdaten = action.raetselgruppe;
            return { ...state, aufgabensammlungBasisdaten: aufgabensammlungBasisdaten };
        }),
        on(raetselgruppenActions.rAETSELGRUPPEDETAILS_LOADED, (state, action) => {
            return {...state, aufgabensammlungDetails: action.aufgabensammlungDetails, aufgabensammlungBasisdaten: undefined};
        }),
        on(raetselgruppenActions.uNSELECT_RAETSELGRUPPE, (state, _action) => {
            return {...state, aufgabensammlungDetails: undefined, aufgabensammlungBasisdaten: undefined, selectedAufgabensammlungselement: undefined, selectedElementImages: undefined}
        }),
        on(raetselgruppenActions.rAETSELGRUPPENELEMENTE_CHANGED, (state, action) => ({ ...state, aufgabensammlungDetails: action.raetselgruppenDetails })),
        on(raetselgruppenActions.sELECT_RAETSELGRUPPENELEMENT, (state, action) => ({...state, selectedAufgabensammlungselement: action.aufgabensammlungselement})),
        on(raetselgruppenActions.eLEMENT_IMAGES_LOADED, (state, action) => ({...state, selectedElementImages: action.generatedImages}))
    )
});