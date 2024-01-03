import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectMjaAuthState, selectSession } = authFeature;

const session = createSelector(
    selectMjaAuthState,
    (state) => state.session
);

const userFull = createSelector(
    selectSession,
    (session) => session.user
);

const benutzerart = createSelector(
    userFull,
    (user) => user.benutzerart
);

const userIsRoot = createSelector(
    benutzerart,
    (benutzerart) => benutzerart === 'ADMIN'

);

const userIsAdmin = createSelector(
    benutzerart,
    (benutzerart) => benutzerart === 'ADMIN' || benutzerart === 'AUTOR'

);

const userIsPublic = createSelector(
    benutzerart,
    (benutzerart) => benutzerart === 'STANDARD'

);


export const fromAuth = {
    selectMjaAuthState,
    userFull,
    benutzerart,
    userIsRoot,
    userIsAdmin,
    userIsPublic,
    session
};
