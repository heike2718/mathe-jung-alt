import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  DESKRIPTOR_FEATURE_KEY,
  DeskriptorenState,
  deskriptorAdapter,
} from './deskriptor.reducer';

// Lookup the 'Deskriptor' feature state managed by NgRx
const getDeskriptorState = createFeatureSelector<DeskriptorenState>(DESKRIPTOR_FEATURE_KEY);

const { selectAll, selectEntities } = deskriptorAdapter.getSelectors();

export const getDeskriptorLoaded = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.loaded
);

export const getAllDeskriptor = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => selectAll(state)
);

export const getDeskriptorEntities = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.selectedId
);

export const getSelected = createSelector(
  getDeskriptorEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

