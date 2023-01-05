import { initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { Raetsel, RaetselDetails, SelectableDeskriptoren } from "@mja-ws/raetsel/model";
import { Action, ActionReducer, createFeature, createReducer, FeatureConfig, on } from "@ngrx/store";
import { raetselActions } from "./raetsel.actions";

export interface RaetselState {
    readonly loaded: boolean;
    readonly page: Raetsel[];
    readonly paginationState: PaginationState;
    readonly selectableDeskriptoren: SelectableDeskriptoren[];
    readonly saveSuccessMessage: string | undefined;
    readonly raetselDetails: RaetselDetails | undefined;
};

const initialState: RaetselState = {
    loaded: false,
    page: [],
    paginationState: initialPaginationState,
    selectableDeskriptoren: [],
    saveSuccessMessage: undefined,
    raetselDetails: undefined
};

export const raetselFeature = createFeature({
    name: 'raetsel',
    reducer: createReducer(
        initialState,
        on(raetselActions.select_page, (state, action): RaetselState => {
            return {
                ...state,
                paginationState: { ...state.paginationState, pageDefinition: {pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection} }
            };
        }),
        on(raetselActions.raetsel_found, (state, action): RaetselState => {
            return {
                ...state,
                loaded: true,
                page: action.treffer.treffer,
                paginationState: {...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt}                
            }
        }),
        on(raetselActions.raetsel_details_loaded, (state, action) => {
            return {
                ...state,
                raetselDetails: action.raetselDetails
            }
        }),
        on(raetselActions.raetselliste_cleared, (state, action): RaetselState => {
            return {
                ...state,
                loaded: false,
                page: [],
                raetselDetails: undefined
            };
        })
    )
});
