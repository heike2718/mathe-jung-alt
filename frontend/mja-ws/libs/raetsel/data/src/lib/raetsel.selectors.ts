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

const raetselDetails = createSelector(
    selectRaetselState,
    (state) => state.raetselDetails
);

const suchfilter = createSelector(
    selectRaetselState,
    (state) => state.raetselSuchfilter
);

export const fromRaetsel = {
    isLoaded,
    page,
    paginationState,
    raetselDetails,
    suchfilter
};

