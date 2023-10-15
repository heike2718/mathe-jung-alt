import { createSelector } from '@ngrx/store';
import { coreDeskriptorenUIFeature } from './core-deskriptoren.reducer';

const { selectMjaCoreDeskriptorenState } = coreDeskriptorenUIFeature;

const isDeskriptorenUILoaded = createSelector(
    selectMjaCoreDeskriptorenState,
    (state) => state.deskriptorenUILoaded
);

const deskriptorenUI = createSelector(
    selectMjaCoreDeskriptorenState,
    (state) => state.deskriptoren
);

export const fromCoreDeskriptoren = {
    isDeskriptorenUILoaded,
    deskriptotrenUI: deskriptorenUI
};

