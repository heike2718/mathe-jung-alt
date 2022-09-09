import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
    RAETSELGRUPPEN_FEATURE_KEY,
    RaetselgruppenState,
    raetselgruppenAdapter
} from './raetselgruppen.reducer';

// Lookup the 'Raetsel' feature state managed by NgRx
export const getRaetselState = createFeatureSelector<RaetselgruppenState>(RAETSELGRUPPEN_FEATURE_KEY);

const { selectAll, selectEntities } = raetselgruppenAdapter.getSelectors();

export const getPaginationState = createSelector(
    getRaetselState,
    (state: RaetselgruppenState) => state.paginationState
);

export const getPage = createSelector(
    getRaetselState,
    (state: RaetselgruppenState) => state.page
);
