import { ActionReducer, INIT, MetaReducer } from '@ngrx/store';
import { authActions } from '@mja-ws/core/data';

function clearState(reducer: ActionReducer<unknown>): ActionReducer<unknown> {
    return (state, action) => {
        if (action != null && action.type === authActions.lOGGED_OUT.type) {
            return reducer(undefined, { type: INIT });
        }
        return reducer(state, action);
    };
}

export const loggedOutMetaReducer: MetaReducer<unknown> = clearState;
