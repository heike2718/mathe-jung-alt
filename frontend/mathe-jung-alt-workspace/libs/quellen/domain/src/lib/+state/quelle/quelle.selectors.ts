import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  QUELLE_FEATURE_KEY,
  State,
  QuellePartialState,
  quelleAdapter,
} from './quelle.reducer';

// Lookup the 'Quelle' feature state managed by NgRx
export const getQuelleState = createFeatureSelector<QuellePartialState, State>(
  QUELLE_FEATURE_KEY
);

const { selectAll, selectEntities } = quelleAdapter.getSelectors();

export const getQuelleLoaded = createSelector(
  getQuelleState,
  (state: State) => state.loaded
);

export const getQuelleError = createSelector(
  getQuelleState,
  (state: State) => state.error
);

export const getAllQuelle = createSelector(getQuelleState, (state: State) =>
  selectAll(state)
);

export const getQuelleEntities = createSelector(
  getQuelleState,
  (state: State) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getQuelleState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getQuelleEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
