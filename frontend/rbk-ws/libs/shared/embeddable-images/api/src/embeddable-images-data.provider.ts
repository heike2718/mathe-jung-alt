import { EmbeddableImagesEffects, embeddableImagesFeature } from '@rbk-ws/embeddable-images/data';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

export const embeddableImagesDataProvider = [
  provideState(embeddableImagesFeature),
  provideEffects(EmbeddableImagesEffects),
];
