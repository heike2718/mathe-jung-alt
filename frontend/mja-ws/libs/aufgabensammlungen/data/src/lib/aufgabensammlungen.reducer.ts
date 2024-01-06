import { GeneratedImages, initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungTrefferItem } from "@mja-ws/aufgabensammlungen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { aufgabensammlungenActions } from "./aufgabensammlungen.actions";
import { swallowEmptyArgument } from "@mja-ws/shared/util";

export interface AufgabensammlungenState {
    readonly loaded: boolean;
    readonly anzahlTrefferGesamt: number;
    readonly page: AufgabensammlungTrefferItem[];
    readonly paginationState: PaginationState;
    readonly aufgabensammlungBasisdaten: AufgabensammlungBasisdaten| undefined;
    readonly aufgabensammlungDetails: AufgabensammlungDetails | undefined;
    readonly selectedAufgabensammlungselement: Aufgabensammlungselement | undefined;
    readonly selectedElementImages: GeneratedImages | undefined;
};

const initialAufgabensammlungenState: AufgabensammlungenState = {
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
    name: 'aufgabensammlungen',
    reducer: createReducer(
        initialAufgabensammlungenState,
        on(aufgabensammlungenActions.aUFGABENSAMMLUNGEN_FOUND, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                page: action.treffer.items,
                loaded: true,
                selectedAufgabensammlungselement: undefined,
                selectedElementImages: undefined
            };
        }),
        on((aufgabensammlungenActions.aUFGABENSAMMLUNGEN_SELECT_PAGE), (state, action) => {
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
        on(aufgabensammlungenActions.eDIT_AUFGABENSAMMLUNG, (state, action) => {
            return {
                ...state,
                aufgabensammlungBasisdaten: action.aufgabensammlung
            }
        }),
        on(aufgabensammlungenActions.aUFGABENSAMMLUNG_SAVED, (state, action) => {
            const aufgabensammlungBasisdaten: AufgabensammlungBasisdaten = action.aufgabensammlung;
            return { ...state, aufgabensammlungBasisdaten: aufgabensammlungBasisdaten };
        }),
        on(aufgabensammlungenActions.aUFGABENSAMMLUNGDETAILS_LOADED, (state, action) => {
            return {...state, aufgabensammlungDetails: action.aufgabensammlungDetails, aufgabensammlungBasisdaten: undefined};
        }),
        on(aufgabensammlungenActions.uNSELECT_AUFGABENSAMMLUNG, (state, action) => {
            swallowEmptyArgument(action, false);
            return {...state, aufgabensammlungDetails: undefined, aufgabensammlungBasisdaten: undefined, selectedAufgabensammlungselement: undefined, selectedElementImages: undefined}
        }),
        on(aufgabensammlungenActions.aUFGABENSAMMLUNGSELEMENTE_CHANGED, (state, action) => ({ ...state, aufgabensammlungDetails: action.aufgabensammlungDetails })),
        on(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNGSELEMENT, (state, action) => ({...state, selectedAufgabensammlungselement: action.aufgabensammlungselement})),
        on(aufgabensammlungenActions.eLEMENT_IMAGES_LOADED, (state, action) => ({...state, selectedElementImages: action.generatedImages}))
    )
});