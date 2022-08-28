import { createFeatureSelector, createSelector } from '@ngrx/store';
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
  state => state.loaded
);


export const getAllRaetsel = createSelector(getRaetselState, state =>
  selectAll(state)
);

export const getRaetselEntities = createSelector(
  getRaetselState,
  state => selectEntities(state)
);

export const getSelectedId = createSelector(
  getRaetselState,
 state => state.selectedId
);

export const getSelected = createSelector(
  getRaetselEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

export const getPaginationState = createSelector(
  getRaetselState,
  (state: RaetselState) => state.paginationState
);

export const getPage = createSelector(
  getRaetselState,
  (state: RaetselState) => state.page
);

export const getRaetselDetails = createSelector(
  getRaetselState,
  (state: RaetselState) => state.raetselDetailsContent.raetsel
);

export const getDetailsContent = createSelector(
  getRaetselState,
  (state: RaetselState) => state.raetselDetailsContent
);

export const getRaetselDeskriptoren = createSelector(
  getRaetselState,
  (state: RaetselState) => state.selectableDeskriptoren
);
