import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { Raetsel } from '../../entities/raetsel';

export const RAETSEL_FEATURE_KEY = 'raetsel-raetsel';

export interface State extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  error?: string | null; // last known error (if any)
  loading: boolean;
  page: Raetsel[];
}

export interface RaetselPartialState {
  readonly [RAETSEL_FEATURE_KEY]: State;
}

export const raetselAdapter: EntityAdapter<Raetsel> =
  createEntityAdapter<Raetsel>();

export const initialState: State = raetselAdapter.getInitialState({
  // set initial required properties
  loaded: false,
  loading: false,
  page: []
});

const raetselReducer = createReducer(
  initialState,

  on(RaetselActions.findRaetsel, (state) => ({
    ...state,
    loaded: false,
    error: null,
    loading: true
  })),

  on(RaetselActions.findRaetselSuccess, (state, { raetsel }) =>

    raetselAdapter.setAll(raetsel, { ...state, loaded: true, loading: false, page: raetsel.slice(0, 5) })

  ),

  on(RaetselActions.findRaetselFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  on(RaetselActions.pageSelected, (state, {raetsel}) => ({
    ...state, page: raetsel
  })),
);

export function reducer(state: State | undefined, action: Action) {
  return raetselReducer(state, action);
}
