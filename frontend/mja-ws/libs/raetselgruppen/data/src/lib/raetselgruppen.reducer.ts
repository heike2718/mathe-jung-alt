import { initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { RaetselgruppeDetails, RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselgruppenActions } from "./raetselgruppen.actions";

export interface RaetselgruppenState {
    readonly loaded: boolean;
    readonly anzahlTrefferGesamt: number;
    readonly page: RaetselgruppenTrefferItem[];
    readonly paginationState: PaginationState;
    readonly raetselgruppeDetails: RaetselgruppeDetails | undefined;
};

const initialRaetselgruppenState: RaetselgruppenState = {
    loaded: false,
    anzahlTrefferGesamt: 0,
    page: [],
    paginationState: initialPaginationState,
    raetselgruppeDetails: undefined
};

export const raetselgruppenFeature = createFeature({
    name: 'raetselgruppen',
    reducer: createReducer(
        initialRaetselgruppenState,
        on(raetselgruppenActions.raetselgruppen_found, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                page: action.treffer.items,
                loaded: true
            };
        }),
        on((raetselgruppenActions.raetselgruppen_select_page), (state, action) => {
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
        on(raetselgruppenActions.raetselgruppedetails_loaded, (state, action) => {
            return {...state, raetselgruppeDetails: action.raetselgruppeDetails};
        }),
        on(raetselgruppenActions.unselect_raetselgruppe, (state, _action) => {
            return {...state, raetselgruppeDetails: undefined}
        })
    )
});