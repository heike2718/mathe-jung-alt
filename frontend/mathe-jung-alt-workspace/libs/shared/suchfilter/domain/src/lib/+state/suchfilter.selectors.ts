import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Suchfilter } from '../entities/suchfilter';
import { SuchfilterState, SUCHFILTER_FEATURE_KEY } from './suchfilter.reducer';

const getSuchfilterState = createFeatureSelector<SuchfilterState>(SUCHFILTER_FEATURE_KEY);

export const getSuchfilter = createSelector(getSuchfilterState, (state: SuchfilterState) => state.filter);

const hasSuchstring = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.suchstring.trim().length > 1);
const hasDeskriptoren = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.deskriptoren.length > 0);

const hasKontext = createSelector(getSuchfilter, (suchfilter: Suchfilter) => suchfilter.kontext !== 'NOOP');

export const isSuchfilterReadyToGo = createSelector(hasKontext, hasSuchstring, hasDeskriptoren,
    (defined: boolean, hasSuchstring: boolean, hasDeskriptoren: boolean) =>
        defined && (hasDeskriptoren || hasSuchstring));

