import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  STICHWORT_FEATURE_KEY,
  State,
  StichwortPartialState,
  stichwortAdapter,
} from './stichwort.reducer';

// Lookup the 'Stichwort' feature state managed by NgRx
export const getStichwortState = createFeatureSelector<
  StichwortPartialState,
  State
>(STICHWORT_FEATURE_KEY);

const { selectAll, selectEntities } = stichwortAdapter.getSelectors();

export const getStichwortLoaded = createSelector(
  getStichwortState,
  (state: State) => state.loaded
);

export const getStichwortError = createSelector(
  getStichwortState,
  (state: State) => state.error
);

export const getAllStichwort = createSelector(
  getStichwortState,
  (state: State) => selectAll(state)
);

export const getStichwortEntities = createSelector(
  getStichwortState,
  (state: State) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getStichwortState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getStichwortEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
