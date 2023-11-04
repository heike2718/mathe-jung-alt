import { createSelector } from "@ngrx/store";
import { embeddableImagesFeature } from './embeddable-images.reducer'

const { selectEmbeddableImagesState } = embeddableImagesFeature;

const embeddableImagesResponse = createSelector(
    selectEmbeddableImagesState,
    ((state) => state.embeddableImagesResponse)
);

const embeddableImageVorschauGeladen = createSelector(
    selectEmbeddableImagesState,
    (state) => state.embeddableImageVorschauGeladen
);

const embeddableImageVorschauPfad = createSelector(
    selectEmbeddableImagesState,
    (state) => state.embeddableImageVorschauPfad
);

const selectedEmbeddableImageVorschau = createSelector(
    selectEmbeddableImagesState,
    (state) => state.selectedEmbeddableImageVorschau
);

export const fromEmbeddableImages = {
    embeddableImagesResponse,
    embeddableImageVorschauGeladen,
    embeddableImageVorschauPfad,
    selectedEmbeddableImageVorschau
};
