import { createFeature, createReducer, on } from "@ngrx/store";
import { swallowEmptyArgument } from "@mja-ws/shared/util";
import { LinkedRaetsel, MediensucheTrefferItem, MediumDto } from "@mja-ws/medien/model";
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
    readonly linkedRaetsel: LinkedRaetsel[];
};

const initialMedienState: MedienState = {
    loaded: false,
    anzahlTrefferGesamt: 0,
    page: [],
    paginationState: initialPaginationState,
    selectedTrefferItem: undefined,
    selectedMediumDetails: undefined,
    allMedienDetails: [],
    linkedRaetsel: []
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

        on(medienActions.eDIT_MEDIUM, (state, action) => {
            return {
                ...state,
                selectedMediumDetails: action.medium
            };
        }),

        on(medienActions.mEDIUMDETAILS_LOADED, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                selectedMediumDetails: action.details
            };
        }),

        on(medienActions.mEDIUM_SAVED, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                selectedMediumDetails: action.medium
            };
        }),

        on(medienActions.uNSELECT_MEDIUM, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                selectedMediumDetails: undefined,
                linkedRaetsel: []
            };
        }),

        on(medienActions.lINKED_RAETSEL_FOUND, (state, action) => {
            return {
                ...state,
                linkedRaetsel: action.raetsel
            };
        })
    )
});