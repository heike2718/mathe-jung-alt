import { createSelector } from "@ngrx/store";
import { raetselFeature } from "./raetsel.reducer";


const { selectRaetselState } = raetselFeature;

const isLoaded = createSelector(
    selectRaetselState,
    (state) => state.loaded
);

const page = createSelector(
    selectRaetselState,
    (state) => state.page
);

const paginationState = createSelector(
    selectRaetselState,
    (state) => state.paginationState
);

export const fromRaetsel = {
    isLoaded,
    page,
    paginationState
};

