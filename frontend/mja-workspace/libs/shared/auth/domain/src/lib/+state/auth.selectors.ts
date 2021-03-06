import { isExpired } from '@mja-workspace/shared/util-mja';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { User, findRole } from '../entities/auth.model';

import {
    AUTH_FEATURE_KEY,
    AuthState,
} from './auth.reducer';

const getAuthState = createFeatureSelector<AuthState>(AUTH_FEATURE_KEY);


export const isLoggedIn = createSelector(getAuthState, (state: AuthState) => state.session !== undefined);
export const isLoggedOut = createSelector(isLoggedIn, loggedIn => !loggedIn);
export const getUser = createSelector(getAuthState, (state: AuthState) => state.session ? state.session.user : undefined);
export const isAnonymousSession = createSelector(getAuthState, (state: AuthState) => state.session === undefined);

const getUserRoles = createSelector(getUser, (user: User | undefined) => user === undefined ? [] : user.roles)


export const isSessionExpired = createSelector(getAuthState, (state: AuthState) => !state.session || isExpired(state.session.expiresAt));
export const isAuthorized = createSelector(isLoggedIn, isSessionExpired, ((loggedIn, isExpired) => loggedIn && !isExpired));
export const isAdmin = createSelector(getUserRoles, (roles: string[]) => findRole('ADMIN', roles));
export const isOrdinaryUser = createSelector(isLoggedIn, isAdmin, (isLoggedIn: boolean, isAdmin: boolean) => isLoggedIn && !isAdmin);
