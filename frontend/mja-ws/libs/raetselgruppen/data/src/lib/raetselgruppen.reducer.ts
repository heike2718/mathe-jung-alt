import { GeneratedImages, initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { RaetselgruppeBasisdaten, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselgruppenActions } from "./raetselgruppen.actions";

export interface RaetselgruppenState {
    readonly loaded: boolean;
    readonly anzahlTrefferGesamt: number;
    readonly page: RaetselgruppenTrefferItem[];
    readonly paginationState: PaginationState;
    readonly raetselgruppeBasisdaten: RaetselgruppeBasisdaten| undefined;
    readonly raetselgruppeDetails: RaetselgruppeDetails | undefined;
    readonly selectedRaetselgruppenelement: Raetselgruppenelement | undefined;
    readonly selectedElementImages: GeneratedImages | undefined;
};

const initialRaetselgruppenState: RaetselgruppenState = {
    loaded: false,
    anzahlTrefferGesamt: 0,
    page: [],
    paginationState: initialPaginationState,
    raetselgruppeBasisdaten: undefined,
    raetselgruppeDetails: undefined,
    selectedRaetselgruppenelement: undefined,
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
                selectedRaetselgruppenelement: undefined,
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
                raetselgruppeBasisdaten: action.raetselgruppeBasisdaten
            }
        }),
        on(raetselgruppenActions.rAETSELGRUPPE_SAVED, (state, action) => {
            const raetselgruppeBasisdaten: RaetselgruppeBasisdaten = action.raetselgruppe;
            return { ...state, raetselgruppeBasisdaten: raetselgruppeBasisdaten };
        }),
        on(raetselgruppenActions.rAETSELGRUPPEDETAILS_LOADED, (state, action) => {
            return {...state, raetselgruppeDetails: action.raetselgruppeDetails, raetselgruppeBasisdaten: undefined};
        }),
        on(raetselgruppenActions.uNSELECT_RAETSELGRUPPE, (state, _action) => {
            return {...state, raetselgruppeDetails: undefined, raetselgruppeBasisdaten: undefined, selectedRaetselgruppenelement: undefined, selectedElementImages: undefined}
        }),
        on(raetselgruppenActions.rAETSELGRUPPENELEMENTE_CHANGED, (state, action) => ({ ...state, raetselgruppeDetails: action.raetselgruppenDetails })),
        on(raetselgruppenActions.sELECT_RAETSELGRUPPENELEMENT, (state, action) => ({...state, selectedRaetselgruppenelement: action.raetselgruppenelement})),
        on(raetselgruppenActions.eLEMENT_IMAGES_LOADED, (state, action) => ({...state, selectedElementImages: action.generatedImages}))
    )
});