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

export const fromRaetsel = {
    isLoaded,
    page
};

