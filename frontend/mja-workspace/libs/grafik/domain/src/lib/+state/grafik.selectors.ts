import { createFeatureSelector, createSelector } from '@ngrx/store';

import { GRAFIK_FEATURE_KEY, GrafikState} from './grafik.reducer';

export const getGrafikState = createFeatureSelector<GrafikState>(GRAFIK_FEATURE_KEY);

// export const isLoading = createSelector(getGrafikState, (state) => state.loading);
export const isLoaded = createSelector(getGrafikState, (state) => state.loaded);
export const getPfad = createSelector(getGrafikState, (state) => state.pfad);
export const getSelectedGrafikSearchResult = createSelector(getGrafikState, (state) => state.selectedGrafikSearchResult);
