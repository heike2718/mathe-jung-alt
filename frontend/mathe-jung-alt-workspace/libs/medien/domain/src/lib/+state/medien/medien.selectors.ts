import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  MEDIEN_FEATURE_KEY,
  State,
  MedienPartialState,
  medienAdapter,
} from './medien.reducer';

// Lookup the 'Medien' feature state managed by NgRx
export const getMedienState = createFeatureSelector<MedienPartialState, State>(
  MEDIEN_FEATURE_KEY
);

const { selectAll, selectEntities } = medienAdapter.getSelectors();

export const getMedienLoaded = createSelector(
  getMedienState,
  (state: State) => state.loaded
);

export const getMedienError = createSelector(
  getMedienState,
  (state: State) => state.error
);

export const getAllMedien = createSelector(getMedienState, (state: State) =>
  selectAll(state)
);

export const getMedienEntities = createSelector(
  getMedienState,
  (state: State) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getMedienState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getMedienEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
