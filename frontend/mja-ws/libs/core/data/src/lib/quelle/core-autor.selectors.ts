import { createSelector } from '@ngrx/store';
import { coreAutorFeature } from './core-autor.reducer';

const { selectMjaCoreAutorState } = coreAutorFeature;

const herkunftEigenkreation = createSelector(
    selectMjaCoreAutorState,
    (state) => state.herkunftEigenkreation
);

const isHerkunftEigenkreation = createSelector(
    selectMjaCoreAutorState,
    (quelleAdminLoaded) => quelleAdminLoaded
);

const existsHerkunftEigenkreation = createSelector(
    herkunftEigenkreation,
    isHerkunftEigenkreation,
    (q, l) => l && q.id !== 'NOOP'
);

const notExistsHerkunftEigenkreation = createSelector(
    herkunftEigenkreation,
    isHerkunftEigenkreation,
    (q, l) => l && q.id === 'NOOP'
);

export const fromCoreAutor = {
    herkunftEigenkreation,
    existsHerkunftEigenkreation,
    notExistsHerkunftEigenkreation
};

