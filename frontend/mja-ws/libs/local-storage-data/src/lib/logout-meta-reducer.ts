import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { authActions } from '@mja-ws/shared/auth/data';

function clearState(reducer: ActionReducer<any>): ActionReducer<any> {
    return (state, action) => {
        if (action != null && action.type === authActions.logged_out.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer: MetaReducer<any> = clearState;
