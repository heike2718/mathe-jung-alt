import { initialPaginationState, PaginationState } from "@mja-ws/core/model";
import { Raetsel, SelectableDeskriptoren } from "@mja-ws/raetsel/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselActions } from "./raetsel.actions";

export interface RaetselState {
    readonly loaded: boolean;
    readonly selectedId?: string;
    readonly page: Raetsel[];
    readonly paginationState: PaginationState;
    readonly selectableDeskriptoren: SelectableDeskriptoren[];
    readonly saveSuccessMessage?: string;
};

const initialRaetselState: RaetselState = {
    loaded: false,
    selectedId: undefined,
    page: [],
    paginationState: initialPaginationState,
    selectableDeskriptoren: [],
    saveSuccessMessage: undefined
};

