import { createFeature, createReducer, on, State } from '@ngrx/store';
import { authActions } from './auth.actions';
import { anonymousSession, Session } from './internal.model';

export interface AuthState {
    readonly session: Session;
};

const initialState: AuthState = {
    session: anonymousSession
};

export const authFeature = createFeature({
    name: 'auth',
    reducer: createReducer<AuthState>(
        initialState,
        on(
            authActions.sessionCreated,
            (state, { session: session }): AuthState => ({
                ...state,
                session: session
            })
        ),
        on(
            authActions.loggedOut,
            (state, _action): AuthState => ({
                ...state,
                session: anonymousSession
            })
        )
    )
});
