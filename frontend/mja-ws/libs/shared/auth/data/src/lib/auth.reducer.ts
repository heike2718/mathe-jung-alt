import { generateUUID } from '@mja-ws/shared/util';
import { createFeature, createReducer, on } from '@ngrx/store';
import { authActions } from './auth.actions';
import { anonymousSession, Session } from './internal.model';
import { swallowEmptyArgument } from '@mja-ws/shared/ngrx-utils';

export interface AuthState {
    readonly session: Session;
};

export const initialState: AuthState = {
    session: anonymousSession
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
