import { EmbeddableImagesEffects, embeddableImagesFeature } from '@mja-ws/embeddable-images/data';
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";

export const embeddableImagesDataProvider = [
    provideState(embeddableImagesFeature),
    provideEffects(EmbeddableImagesEffects)
];