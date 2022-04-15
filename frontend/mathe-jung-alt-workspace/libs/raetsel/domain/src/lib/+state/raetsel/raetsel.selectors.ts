import { Renderer2 } from '@angular/core';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Raetsel } from '../../entities/raetsel';
import {
  RAETSEL_FEATURE_KEY,
  RaetselState,
  raetselAdapter,
} from './raetsel.reducer';

// Lookup the 'Raetsel' feature state managed by NgRx
export const getRaetselState = createFeatureSelector<RaetselState>(RAETSEL_FEATURE_KEY);

const { selectAll, selectEntities } = raetselAdapter.getSelectors();

export const getRaetselLoaded = createSelector(
  getRaetselState,
  (state: RaetselState) => state.loaded
);

export const getAllRaetsel = createSelector(getRaetselState, (state: RaetselState) =>
  selectAll(state)
);

export const getRaetselEntities = createSelector(
  getRaetselState,
  (state: RaetselState) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getRaetselState,
  (state: RaetselState) => state.selectedId
);

export const getSelected = createSelector(
  getRaetselEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

export const getPage = createSelector(
  getRaetselState,
  (state: RaetselState) => state.page
);
