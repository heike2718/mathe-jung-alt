import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
    RAETSELGRUPPEN_FEATURE_KEY,
    RaetselgruppenState
} from './raetselgruppen.reducer';

// Lookup the 'Raetselgruppen' feature state managed by NgRx
const getRaetselgruppenState = createFeatureSelector<RaetselgruppenState>(RAETSELGRUPPEN_FEATURE_KEY);

export const getRaetselgruppenSuchparameter = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.suchparameter
);

export const getAnzahlTrefferGesamt = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.anzahlTrefferGesamt
);

export const getPage = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.page
);

export const getSelectedGruppe = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.selectedGruppe
);

