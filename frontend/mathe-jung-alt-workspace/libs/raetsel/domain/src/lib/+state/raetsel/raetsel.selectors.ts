import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  RAETSEL_FEATURE_KEY,
  State,
  RaetselPartialState,
  raetselAdapter,
} from './raetsel.reducer';

// Lookup the 'Raetsel' feature state managed by NgRx
export const getRaetselState = createFeatureSelector<
  RaetselPartialState,
  State
>(RAETSEL_FEATURE_KEY);

const { selectAll, selectEntities } = raetselAdapter.getSelectors();

export const getRaetselLoaded = createSelector(
  getRaetselState,
  (state: State) => state.loaded
);

export const getRaetselError = createSelector(
  getRaetselState,
  (state: State) => state.error
);

export const getAllRaetsel = createSelector(getRaetselState, (state: State) =>
  selectAll(state)
);

export const getRaetselEntities = createSelector(
  getRaetselState,
  (state: State) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getRaetselState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getRaetselEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
