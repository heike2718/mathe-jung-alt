import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Deskriptor, findSuchfilterUIModelWithKontext, getDifferenzmenge, Suchfilter, SuchfilterUIModel, suchkriterienVorhanden } from '../entities/suchfilter';
import { SuchfilterState, SUCHFILTER_FEATURE_KEY } from './suchfilter.reducer';

const getSuchfilterState = createFeatureSelector<SuchfilterState>(SUCHFILTER_FEATURE_KEY);

export const deskriptorenLoaded = createSelector(getSuchfilterState, state => state.loaded);

export const getAllDeskriptoren = createSelector(getSuchfilterState, state => state.deskriptoren);

export const getSelectedSuchfilterUIModel = createSelector(getSuchfilterState, (state: SuchfilterState) => findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels));
export const getSelectedSuchfilter = createSelector(getSelectedSuchfilterUIModel, (model?: SuchfilterUIModel) => model ? model.suchfilter : undefined);

export const getFilteredDeskriptoren = createSelector(getSelectedSuchfilterUIModel, (model?: SuchfilterUIModel) => model? model.filteredDeskriptoren : []);
export const getSuchliste = createSelector(getSelectedSuchfilter, (suchfilter?: Suchfilter) => suchfilter ? suchfilter.deskriptoren : []);

const suchfilterChanged = createSelector(getSelectedSuchfilterUIModel, (model?: SuchfilterUIModel) => model ? model.changed : false);

export const isSuchfilterReadyToGo = createSelector(
    getSelectedSuchfilter,
    suchfilterChanged,
    (suchfilter: Suchfilter | undefined, changed: boolean) =>
        changed && suchkriterienVorhanden(suchfilter));


export const getRestliste = createSelector(
    getFilteredDeskriptoren,
    getSuchliste,
    (deskriptoren: Deskriptor[], suchliste: Deskriptor[]) => getDifferenzmenge(deskriptoren, suchliste)
);

