import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as QuelleActions from './quelle.actions';
import { Quelle } from '../../entities/quelle';

export const QUELLE_FEATURE_KEY = 'quellen-quelle';

export interface State extends EntityState<Quelle> {
  selectedId?: string | number; // which Quelle record has been selected
  loaded: boolean; // has the Quelle list been loaded
  error?: string | null; // last known error (if any)
}

export interface QuellePartialState {
  readonly [QUELLE_FEATURE_KEY]: State;
}

export const quelleAdapter: EntityAdapter<Quelle> =
  createEntityAdapter<Quelle>();

export const initialState: State = quelleAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const quelleReducer = createReducer(
  initialState,
  on(QuelleActions.loadQuelle, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(QuelleActions.loadQuelleSuccess, (state, { quelle }) =>
    quelleAdapter.upsertMany(quelle, { ...state, loaded: true })
  ),
  on(QuelleActions.loadQuelleFailure, (state, { error }) => ({
    ...state,
    error,
  }))
);

export function reducer(state: State | undefined, action: Action) {
  return quelleReducer(state, action);
}
