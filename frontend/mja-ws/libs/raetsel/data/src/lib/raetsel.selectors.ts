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

const generateLatexError = createSelector(
    selectRaetselState,
    (state) => state.generateLatexError
);

const medienForQuelle = createSelector(
    selectRaetselState,
    (state) => state.medienForQuelle
);

const herkunftstyp = createSelector(
    raetselDetails,
    (details) => details ? details.herkunftstyp : 'EIGENKREATION'
);

const showQuelle = createSelector(
    herkunftstyp,
    (herkunftstyp) => herkunftstyp === 'EIGENKREATION' ? false : true
);

export const fromRaetsel = {
    isLoaded,
    page,
    paginationState,
    raetselDetails,
    suchfilter,
    generateLatexError,
    medienForQuelle,
    showQuelle
};

