import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  QUELLE_FEATURE_KEY,
  QuellenState,
  quelleAdapter,
} from './quelle.reducer';

// Lookup the 'Quelle' feature state managed by NgRx
export const getQuelleState = createFeatureSelector<QuellenState>(QUELLE_FEATURE_KEY);

const { selectAll, selectEntities } = quelleAdapter.getSelectors();

export const getQuelleLoaded = createSelector(
  getQuelleState,
  (state: QuellenState) => state.loaded
);

export const getAllQuellen = createSelector(getQuelleState, (state: QuellenState) =>
  selectAll(state)
);

export const getQuelleEntities = createSelector(
  getQuelleState,
  (state: QuellenState) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getQuelleState,
  (state: QuellenState) => state.selectedId
);

export const getSelected = createSelector(
  getQuelleEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

export const getPage = createSelector(
  getQuelleState,
  (state: QuellenState) => state.page
);
