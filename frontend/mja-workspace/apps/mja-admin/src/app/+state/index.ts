import { LOGOUT_ACTION_TYPE } from '@mja-workspace/shared/auth/domain';
import { ActionReducer, ActionReducerMap, INIT, MetaReducer } from '@ngrx/store';
import { environment } from '../../environments/environment';


export interface AppState { }

export const reducers: ActionReducerMap<AppState> = {};

export function logout(reducer: ActionReducer<AppState>): ActionReducer<AppState> {
  return (state, action) => {
    if ( action != null && action.type === LOGOUT_ACTION_TYPE) {
      return reducer( undefined, {type: INIT});
    }
    return reducer(state, action);
  };
}

export const metaReducers: MetaReducer<AppState>[] = !environment.production ? [logout] : [];
