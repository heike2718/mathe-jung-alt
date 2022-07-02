import { createReducer, on, Action } from '@ngrx/store';
import { Session } from '../entities/auth.model';
import * as AuthActions from './auth.actions';

export const AUTH_FEATURE_KEY = 'auth';

export interface AuthPartialState {
    readonly [AUTH_FEATURE_KEY]: AuthState;
}

export interface AuthState {
    readonly session?: Session;
};

const initialState: AuthState = {
    session: undefined
};

const authReducer = createReducer(initialState,

    on(AuthActions.sessionCreated, (state, action) => {
        return { ...state, session: action.session };
    }),

    on(AuthActions.sessionRestored, (state, action) => {
        return { ...state, session: action.session };
    }),

    on(AuthActions.sessionCleared, (state, _action) => {
        return { ...state, session: undefined };
    }),
    
    on(AuthActions.userLoggedOut, (state, _action) => {
        return { ...state, session: undefined };
    }),
);

export function reducer(state: AuthState | undefined, action: Action) {
    return authReducer(state, action);
};

