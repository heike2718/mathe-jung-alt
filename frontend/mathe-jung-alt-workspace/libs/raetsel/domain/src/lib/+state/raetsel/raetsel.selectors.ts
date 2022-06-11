import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  RAETSEL_FEATURE_KEY,
  RaetselState,
  raetselAdapter,
} from './raetsel.reducer';

// Lookup the 'Raetsel' feature state managed by NgRx
export const getRaetselState = createFeatureSelector<RaetselState>(RAETSEL_FEATURE_KEY);

const { selectEntities } = raetselAdapter.getSelectors();

export const getRaetselLoaded = createSelector(
  getRaetselState,
  (state: RaetselState) => state.loaded
);

export const getRaetselEntities = createSelector(
  getRaetselState,
  (state: RaetselState) => selectEntities(state)
);

export const getPaginationState = createSelector(
  getRaetselState,
  (state: RaetselState) => state.paginationState
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

export const getRaetselDetails = createSelector(
  getRaetselState,
  (state: RaetselState) => state.raetselDetails
);

export const getSaveSuccessMessage = createSelector(
  getRaetselState,
  (state: RaetselState) => state.saveSuccessMessage
);

export const getEditorContent = createSelector(
  getRaetselState,
  (state: RaetselState) => state.raetselEditorContent
);

export const generatingOutput = createSelector (
  getRaetselState,
  (state:RaetselState) => state.generatingOutput
);
