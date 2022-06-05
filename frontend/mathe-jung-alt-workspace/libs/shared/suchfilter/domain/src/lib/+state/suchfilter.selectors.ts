import { Deskriptor, getDifferenzmenge } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { findSuchfilterUIModelWithKontext, Suchfilter, SuchfilterUIModel } from '../entities/suchfilter';
import { SuchfilterState, SUCHFILTER_FEATURE_KEY } from './suchfilter.reducer';

const getSuchfilterState = createFeatureSelector<SuchfilterState>(SUCHFILTER_FEATURE_KEY);

export const getDeskriptorenLoaded = createSelector(getSuchfilterState, (state: SuchfilterState) => state.deskriptorenLoaded);

export const getAllDeskriptoren = createSelector(getSuchfilterState, (state: SuchfilterState) => state.deskriptoren);

export const getSelectedSuchfilterUIModel = createSelector(getSuchfilterState, (state: SuchfilterState) => findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels));
export const getSelectedSuchfilter = createSelector(getSelectedSuchfilterUIModel, (model?: SuchfilterUIModel) => model ? model.suchfilter : undefined);

export const getFilteredDeskriptoren = createSelector(getSelectedSuchfilterUIModel, (model?: SuchfilterUIModel) => model? model.filteredDeskriptoren : []);
export const getSuchliste = createSelector(getSelectedSuchfilter, (suchfilter?: Suchfilter) => suchfilter ? suchfilter.deskriptoren : []);

// // Worte mit weniger als 4 Zeichen sind nicht Teil des Volltextindex. Daher erst fertig, wenn mindestens 4 Zeichen
const hasSuchstring = createSelector(getSelectedSuchfilter, (suchfilter?: Suchfilter) => suchfilter ? suchfilter.suchstring.trim().length > 3 : false);
const hasDeskriptoren = createSelector(getSuchliste, (suchliste: Deskriptor[]) => suchliste.length > 0);

export const isSuchfilterReadyToGo = createSelector(
    getSelectedSuchfilter,
    hasSuchstring,
    hasDeskriptoren,
    (suchfilter: Suchfilter | undefined, hasSuchstring: boolean, hasDeskriptoren: boolean) =>
        suchfilter !== undefined && suchfilter.kontext !== 'NOOP' && (hasDeskriptoren || hasSuchstring));

export const getRestliste = createSelector(
    getFilteredDeskriptoren,
    getSuchliste,
    (deskriptoren: Deskriptor[], suchliste: Deskriptor[]) => getDifferenzmenge(deskriptoren, suchliste)
);

