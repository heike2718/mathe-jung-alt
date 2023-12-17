import { createFeature, createReducer, on } from "@ngrx/store";
import { swallowEmptyArgument } from "@mja-ws/shared/util";
import { MediensucheTrefferItem, MediumDto } from "@mja-ws/medien/model";
import { PaginationState, initialPaginationState } from "@mja-ws/core/model";
import { medienActions } from "./medien.actions";

export interface MedienState {
    readonly loaded: boolean;
    readonly anzahlTrefferGesamt: number;
    readonly page: MediensucheTrefferItem[];
    readonly paginationState: PaginationState;
    readonly selectedTrefferItem: MediensucheTrefferItem | undefined;
    readonly selectedMediumDetails: MediumDto | undefined;
    readonly allMedienDetails: MediumDto[];
};

const initialMedienState: MedienState = {
    loaded: false,
    anzahlTrefferGesamt: 0,
    page: [],
    paginationState: initialPaginationState,
    selectedTrefferItem: undefined,
    selectedMediumDetails: undefined,
    allMedienDetails: []
};

export const medienFeature = createFeature({
    name: 'medien',
    reducer: createReducer(
        initialMedienState,
        on(medienActions.mEDIEN_FOUND, (state, action) => {
            return {
                ...state,
                paginationState: { ...state.paginationState, anzahlTreffer: action.result.trefferGesamt },
                page: action.result.treffer,
                loaded: true
            };
        }),
    )
});