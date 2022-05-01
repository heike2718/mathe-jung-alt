import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  MEDIEN_FEATURE_KEY,
  MedienState,
  MedienPartialState,
  medienAdapter,
} from './medien.reducer';

// Lookup the 'Medien' feature state managed by NgRx
export const getMedienState = createFeatureSelector<MedienPartialState, MedienState>(
  MEDIEN_FEATURE_KEY
);

const { selectAll, selectEntities } = medienAdapter.getSelectors();

export const getMedienLoaded = createSelector(
  getMedienState,
  (state: MedienState) => state.loaded
);

export const getMedienError = createSelector(
  getMedienState,
  (state: MedienState) => state.error
);

export const getAllMedien = createSelector(getMedienState, (state: MedienState) =>
  selectAll(state)
);

export const getMedienEntities = createSelector(
  getMedienState,
  (state: MedienState) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getMedienState,
  (state: MedienState) => state.selectedId
);

export const getSelected = createSelector(
  getMedienEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
