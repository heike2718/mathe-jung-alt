import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Suchfilter } from '../entities/suchfilter';
import { SuchfilterState, SUCHFILTER_FEATURE_KEY } from './suchfilter.reducer';

const getSuchfilterState = createFeatureSelector<SuchfilterState>(SUCHFILTER_FEATURE_KEY);

export const getSuchfilter = createSelector(getSuchfilterState, (state: SuchfilterState) => state.filter);
export const getSuchfilterKontext = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.kontext);

// Worte mit weniger als 4 Zeichen sind nicht Teil des Volltextindex. Daher erst fertig, wenn mindestens 4 Zeichen
const hasSuchstring = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.suchstring.trim().length > 3);
const hasDeskriptoren = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.deskriptoren.length > 0);

const hasKontext = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.kontext !== 'NOOP');
 

export const isSuchfilterReadyToGo = createSelector(hasKontext, hasSuchstring, hasDeskriptoren,
    (defined: boolean, hasSuchstring: boolean, hasDeskriptoren: boolean) =>
        defined && (hasDeskriptoren || hasSuchstring));

export const getSuchfilterAndReady = createSelector(getSuchfilter, isSuchfilterReadyToGo, 
    (suchfilter: Suchfilter, nichtLeer: boolean) => ({ suchfilter, nichtLeer }));

