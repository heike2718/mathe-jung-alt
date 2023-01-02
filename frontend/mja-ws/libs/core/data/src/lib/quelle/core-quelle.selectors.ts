import { createSelector } from '@ngrx/store';
import { coreQuelleFeature } from './core-quelle.reducer';

const { selectCoreQuelleState } = coreQuelleFeature;

const quelleAdmin = createSelector(
    selectCoreQuelleState,
    (state) => state.quelleAdmin
);

const isQuelleAdminLoaded = createSelector(
    selectCoreQuelleState,
    (quelleAdminLoaded) => quelleAdminLoaded
);

const existsQuelleAdmin = createSelector(
    quelleAdmin,
    isQuelleAdminLoaded,
    (q, l) => l && q.id !== 'NOOP'
);

const notExistsQuelleAdmin = createSelector(
    quelleAdmin,
    isQuelleAdminLoaded,
    (q, l) => l && q.id === 'NOOP'
);

export const fromCoreQuelle = {
    quelleAdmin,
    existsQuelleAdmin,
    notExistsQuelleAdmin
};

