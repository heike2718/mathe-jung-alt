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

const selectedEmbeddableImageVorschau = createSelector(
    selectGrafikState,
    (state) => state.selectedEmbeddableImageVorschau
);

const selectGrafikHochgeladenMessage = createSelector(
    selectGrafikState,
    (state) => state.grafikHochgeladenMessage
)

export const fromGrafik = {
    isLoaded,
    grafikPfad,
    selectedEmbeddableImageVorschau,
    selectGrafikHochgeladenMessage
};

