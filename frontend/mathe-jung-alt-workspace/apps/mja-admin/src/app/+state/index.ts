import { ActionReducer, ActionReducerMap, INIT, MetaReducer } from '@ngrx/store';
import { logoutAction } from './app.actions';
import { environment } from '../../environments/environment';


export interface AppState { }

export const reducers: ActionReducerMap<AppState> = {};

export function logout(reducer: ActionReducer<AppState>): ActionReducer<AppState> {
  return (state, action) => {
    if ( action != null && action.type === logoutAction.type) {
      return reducer( undefined, {type: INIT});
    }
    return reducer(state, action);
  };
}

export const metaReducers: MetaReducer<AppState>[] = !environment.production ? [logout] : [];
