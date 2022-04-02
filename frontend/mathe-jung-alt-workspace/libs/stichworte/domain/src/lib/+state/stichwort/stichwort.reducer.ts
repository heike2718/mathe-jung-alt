import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as StichwortActions from './stichwort.actions';
import { Stichwort } from '../../entities/stichwort';

export const STICHWORT_FEATURE_KEY = 'stichworte-stichwort';

export interface State extends EntityState<Stichwort> {
  selectedId?: string | number; // which Stichwort record has been selected
  loaded: boolean; // has the Stichwort list been loaded
  error?: string | null; // last known error (if any)
}

export interface StichwortPartialState {
  readonly [STICHWORT_FEATURE_KEY]: State;
}

export const stichwortAdapter: EntityAdapter<Stichwort> =
  createEntityAdapter<Stichwort>();

export const initialState: State = stichwortAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const stichwortReducer = createReducer(
  initialState,
  on(StichwortActions.loadStichwort, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(StichwortActions.loadStichwortSuccess, (state, { stichwort }) =>
    stichwortAdapter.upsertMany(stichwort, { ...state, loaded: true })
  ),
  on(StichwortActions.loadStichwortFailure, (state, { error }) => ({
    ...state,
    error,
  }))
);

export function reducer(state: State | undefined, action: Action) {
  return stichwortReducer(state, action);
}
