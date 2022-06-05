import { Deskriptor, getDifferenzmenge } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { SuchfilterState, SUCHFILTER_FEATURE_KEY } from './suchfilter.reducer';

const getSuchfilterState = createFeatureSelector<SuchfilterState>(SUCHFILTER_FEATURE_KEY);

export const getDeskriptorenLoaded = createSelector(getSuchfilterState, (state: SuchfilterState) => state.deskriptorenLoaded);
export const getSuchfilterKontext = createSelector(getSuchfilterState, (state: SuchfilterState) => state.kontext);

export const getFilteredDeskriptoren = createSelector(getSuchfilterState, (state: SuchfilterState) => state.filteredDeskriptoren);
export const getSuchliste = createSelector(getSuchfilterState, (state: SuchfilterState) => state.suchliste);

// Worte mit weniger als 4 Zeichen sind nicht Teil des Volltextindex. Daher erst fertig, wenn mindestens 4 Zeichen
const hasSuchstring = createSelector(getSuchfilterState, (state: SuchfilterState) => state.suchstring.trim().length > 3);
const hasDeskriptoren = createSelector(getSuchfilterState, (state: SuchfilterState) => state.suchliste.length > 0);

const hasKontext = createSelector(getSuchfilterState, (state: SuchfilterState) => state.kontext !== 'NOOP');


export const isSuchfilterReadyToGo = createSelector(hasKontext, hasSuchstring, hasDeskriptoren,
    (defined: boolean, hasSuchstring: boolean, hasDeskriptoren: boolean) =>
        defined && (hasDeskriptoren || hasSuchstring));

export const getSuchfilterAndReady = createSelector(getSuchfilterState, isSuchfilterReadyToGo,
    (state: SuchfilterState, nichtLeer: boolean) => ({ suchfilter: {kontext: state.kontext, suchstring: state.suchstring, deskriptoren: state.suchliste}, nichtLeer: nichtLeer }));

export const getRestliste = createSelector(
    getFilteredDeskriptoren,
    getSuchliste,
    (deskriptoren: Deskriptor[], suchliste: Deskriptor[]) => getDifferenzmenge(deskriptoren, suchliste)
);

