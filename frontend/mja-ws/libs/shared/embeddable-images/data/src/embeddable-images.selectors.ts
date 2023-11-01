import { createSelector } from "@ngrx/store";
import { embeddableImagesFeature } from './embeddable-images.reducer'

const { selectEmbeddableImagesState } = embeddableImagesFeature;

const embeddableImagesResponse = createSelector(
    selectEmbeddableImagesState,
    ((state) => state.embeddableImagesResponse)
);

export const fromEmbeddableImages = {
    embeddableImagesResponse
};
