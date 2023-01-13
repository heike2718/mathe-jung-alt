import { initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { RaetselgruppensucheTrefferItem } from "@mja-ws/raetselgruppen/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselgruppenActions } from "./raetselgruppen.actions";

export interface RaetselgruppenState {
    readonly loaded: boolean;
    readonly page: RaetselgruppensucheTrefferItem[];
    readonly paginationState: PaginationState;
};

const initialRaetselgruppenState: RaetselgruppenState = {
    loaded: false,
    page: [],
    paginationState: initialPaginationState
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
            }
        })
    )
});