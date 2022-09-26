import { createFeatureSelector, createSelector } from '@ngrx/store';
import { of } from 'rxjs';
import { RaetselgruppeDetails } from '../entities/raetselgruppen';
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

export const getRaetselgruppeBasisdaten = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.raetselgruppeBasisdaten
);

export const getRaetselgruppeDetails = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.raetselgruppeDetails
);

export const getRaetselgruppenelemente = createSelector(
    getRaetselgruppenState,
    (state: RaetselgruppenState) => state.raetselgruppeDetails ? state.raetselgruppeDetails.elemente : []
);

