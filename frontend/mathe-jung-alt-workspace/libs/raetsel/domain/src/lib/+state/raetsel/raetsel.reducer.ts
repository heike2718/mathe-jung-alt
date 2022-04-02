import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { Raetsel, RaetselDetails } from '../../entities/raetsel';

export const RAETSEL_FEATURE_KEY = 'raetsel-raetsel';

export interface State extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  error?: string | null; // last known error (if any)
}

export interface RaetselPartialState {
  readonly [RAETSEL_FEATURE_KEY]: State;
}

export const raetselAdapter: EntityAdapter<Raetsel> =
  createEntityAdapter<Raetsel>();

export const initialState: State = raetselAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const raetselReducer = createReducer(
  initialState,
  on(RaetselActions.loadRaetsel, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(RaetselActions.loadRaetselSuccess, (state, { raetsel }) =>
    raetselAdapter.upsertMany(raetsel, { ...state, loaded: true })
  ),
  on(RaetselActions.loadRaetselFailure, (state, { error }) => ({
    ...state,
    error,
  }))
);

export function reducer(state: State | undefined, action: Action) {
  return raetselReducer(state, action);
}
