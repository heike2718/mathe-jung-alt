import { createFeatureSelector, createSelector } from '@ngrx/store';

import {
    AUTH_FEATURE_KEY,
    AuthState,
} from './auth.reducer';

const getAuthState = createFeatureSelector<AuthState>(AUTH_FEATURE_KEY);

export const isLoggedIn = createSelector(getAuthState, (state: AuthState) => state.session !== undefined);
export const isLoggedOut = createSelector(getAuthState, (state: AuthState) => state.session === undefined);
