import { createSelector } from '@ngrx/store';
import { statistikFeature } from './core-statistik.reducer';


const { selectStatistikState } = statistikFeature;

const isAnzahlPublicRaetselLoaded = createSelector(
    selectStatistikState,
    (state) => state.anzahlPublicRaetselLoaded
);

const anzahlPublicRaetsel = createSelector(
    selectStatistikState,
    (state) => state.anzahlPublicRaetsel
);

export const fromStatistik = {
    isAnzahlPublicRaetselLoaded,
    anzahlPublicRaetsel
};
