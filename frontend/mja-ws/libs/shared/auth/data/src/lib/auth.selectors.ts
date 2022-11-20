import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

const { selectAuthState, selectSession } = authFeature;

const user = createSelector(
    selectSession,
    (session) => session.user
);

export const fromAuth = {
    selectAuthState,
    selectSession,
    user
};
