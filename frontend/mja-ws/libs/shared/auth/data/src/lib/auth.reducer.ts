import { generateUUID } from '@mja-ws/shared/util';
import { createFeature, createReducer, on } from '@ngrx/store';
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
            authActions.session_created,
            (state, { session: session }): AuthState => {

                const correlationId = generateUUID();

                localStorage.setItem('corellationId', correlationId);


                return {
                    ...state,
                    session: session
                };
            }
        ),
        on(
            authActions.logged_out,
            (state, _action): AuthState => ({
                ...state,
                session: anonymousSession
            })
        )
    )
});
