import { createSelector } from "@ngrx/store";
import { grafikFeature } from "./grafik.reducer";

const { selectGrafikState } = grafikFeature;

const isLoaded = createSelector(
    selectGrafikState,
    (state) => state.loaded
);

const grafikPfad = createSelector(
    selectGrafikState,
    (state) => state.pfad
);

const selectedGrafikSearchResult = createSelector(
    selectGrafikState,
    (state) => state.selectedGrafikSearchResult
);

export const fromGrafik = {
    isLoaded,
    grafikPfad,
    selectedGrafikSearchResult
};

