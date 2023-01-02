import { createSelector } from '@ngrx/store';
import { coreDeskriptorenUIFeature } from './core-deskriptoren.reducer';

const { selectCoreDeskriptorenState } = coreDeskriptorenUIFeature;

const isDeskriptorenUILoaded = createSelector(
    selectCoreDeskriptorenState,
    (deskriptorenLoaded) => deskriptorenLoaded
);

const deskriptorenUI = createSelector(
    selectCoreDeskriptorenState,
    (deskriptoren) => deskriptoren
);

export const fromCoreQuelle = {
    isDeskriptorenUILoaded,
    deskriptotrenUI: deskriptorenUI
};

