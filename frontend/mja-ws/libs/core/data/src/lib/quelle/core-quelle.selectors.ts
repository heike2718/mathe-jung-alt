import { createSelector } from '@ngrx/store';
import { coreQuelleFeature } from './core-quelle.reducer';

const { selectMjaCoreQuelleState } = coreQuelleFeature;

const quelleAdmin = createSelector(
    selectMjaCoreQuelleState,
    (state) => state.quelleAdmin
);

const isQuelleAdminLoaded = createSelector(
    selectMjaCoreQuelleState,
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

