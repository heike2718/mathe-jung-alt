import { createSelector } from '@ngrx/store';
import { coreAutorFeature } from './core-autor.reducer';

const { selectMjaCoreAutorState } = coreAutorFeature;

const quelleEigenkreation = createSelector(
    selectMjaCoreAutorState,
    (state) => state.quelleEigenkreation
);

const quelleEigenkreationLoaded = createSelector(
    selectMjaCoreAutorState,
    (state) => state.quelleEigenkreationLoaded
);

const existsQuelleEigenkreation = createSelector(
    quelleEigenkreation,
    quelleEigenkreationLoaded,
    (q, l) => l && q.id !== 'NOOP'
);

const notExistsQuelleEigenkreation = createSelector(
    quelleEigenkreation,
    quelleEigenkreationLoaded,
    (q, l) => l && q.id === 'NOOP'
);

export const fromCoreAutor = {
    quelleEigenkreationLoaded,
    quelleEigenkreation,
    existsQuelleEigenkreation,
    notExistsQuelleEigenkreation
};

