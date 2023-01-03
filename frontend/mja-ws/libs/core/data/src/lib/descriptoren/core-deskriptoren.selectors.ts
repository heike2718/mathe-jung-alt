import { createSelector } from '@ngrx/store';
import { coreDeskriptorenUIFeature } from './core-deskriptoren.reducer';

const { selectCoreDeskriptorenState } = coreDeskriptorenUIFeature;

const isDeskriptorenUILoaded = createSelector(
    selectCoreDeskriptorenState,
    (state) => state.deskriptorenUILoaded
);

const deskriptorenUI = createSelector(
    selectCoreDeskriptorenState,
    (state) => state.deskriptoren
);

export const fromCoreDeskriptoren = {
    isDeskriptorenUILoaded,
    deskriptotrenUI: deskriptorenUI
};

