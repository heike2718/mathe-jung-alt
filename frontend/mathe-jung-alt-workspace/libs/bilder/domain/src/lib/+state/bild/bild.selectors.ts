import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  BILD_FEATURE_KEY,
  State,
  BildPartialState,
  bildAdapter,
} from './bild.reducer';

// Lookup the 'Bild' feature state managed by NgRx
export const getBildState = createFeatureSelector<BildPartialState, State>(
  BILD_FEATURE_KEY
);

const { selectAll, selectEntities } = bildAdapter.getSelectors();

export const getBildLoaded = createSelector(
  getBildState,
  (state: State) => state.loaded
);

export const getBildError = createSelector(
  getBildState,
  (state: State) => state.error
);

export const getAllBild = createSelector(getBildState, (state: State) =>
  selectAll(state)
);

export const getBildEntities = createSelector(getBildState, (state: State) =>
  selectEntities(state)
);

export const getSelectedId = createSelector(
  getBildState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getBildEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);
