import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  BILD_FEATURE_KEY,
  BilderState,
  BildPartialState,
  bildAdapter,
} from './bild.reducer';

// Lookup the 'Bild' feature state managed by NgRx
export const getBildState = createFeatureSelector<BildPartialState, BilderState>(
  BILD_FEATURE_KEY
);

const { selectAll, selectEntities } = bildAdapter.getSelectors();

export const getBildLoaded = createSelector(
  getBildState,
  (state: BilderState) => state.loaded
);

export const getBildError = createSelector(
  getBildState,
  (state: BilderState) => state.error
);

export const getAllBild = createSelector(getBildState, (state: BilderState) =>
  selectAll(state)
);

export const getBildEntities = createSelector(getBildState, (state: BilderState) =>
  selectEntities(state)
);

export const getSelectedId = createSelector(
  getBildState,
  (state: BilderState) => state.selectedId
);

export const getSelected = createSelector(
  getBildEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
