import { generateUUID, swallowEmptyArgument } from '@mja-ws/shared/util';
import { createFeature, createReducer, on } from '@ngrx/store';
import { authActions } from './auth.actions';
import { Session, anonymousSession } from '@mja-ws/core/model';

export interface AuthState {
    readonly session: Session;
    readonly sessionExists: boolean;
};

export const initialState: AuthState = {
    session: anonymousSession,
    sessionExists: false
};

export const authFeature = createFeature({
    name: 'mjaAuth',
    reducer: createReducer<AuthState>(
        initialState,
        on(
            authActions.sESSION_CREATED,
            (state, { session: session }): AuthState => {

                const correlationId = generateUUID();

                localStorage.setItem('mjaCorellationId', correlationId);


                return {
                    ...state,
                    session: session
                };
            }
        ),
        on(authActions.lOGGED_OUT, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                session: anonymousSession
            }
        })
    )
});
