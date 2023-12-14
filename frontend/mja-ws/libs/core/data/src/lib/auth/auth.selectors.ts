import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectMjaAuthState, selectSession } = authFeature;

const user = createSelector(
    selectSession,
    (session) => session.user
);

const benutzerart = createSelector(
    user,
    (user) => user.benutzerart
);

export const fromAuth = {
    selectMjaAuthState,
    selectSession,
    user,
    benutzerart
};
