import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as DeskriptorActions from './deskriptor.actions';
import { Deskriptor } from '../../entities/deskriptor';

export const DESKRIPTOR_FEATURE_KEY = 'deskriptoren-deskriptor';

export interface State extends EntityState<Deskriptor> {
  selectedId?: string | number; // which Deskriptor record has been selected
  loaded: boolean; // has the Deskriptor list been loaded
  error?: string | null; // last known error (if any)
}

export interface DeskriptorPartialState {
  readonly [DESKRIPTOR_FEATURE_KEY]: State;
}

export const deskriptorAdapter: EntityAdapter<Deskriptor> =
  createEntityAdapter<Deskriptor>();

export const initialState: State = deskriptorAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const deskriptorReducer = createReducer(
  initialState,
  on(DeskriptorActions.loadDeskriptor, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(DeskriptorActions.loadDeskriptorSuccess, (state, { deskriptor }) =>
    deskriptorAdapter.upsertMany(deskriptor, { ...state, loaded: true })
  ),
  on(DeskriptorActions.loadDeskriptorFailure, (state, { error }) => ({
    ...state,
    error,
  }))
);

export function reducer(state: State | undefined, action: Action) {
  return deskriptorReducer(state, action);
}
