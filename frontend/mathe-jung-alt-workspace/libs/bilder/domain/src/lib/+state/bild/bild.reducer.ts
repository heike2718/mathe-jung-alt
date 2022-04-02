import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as BildActions from './bild.actions';
import { Bild } from '../../entities/bild';

export const BILD_FEATURE_KEY = 'bilder-bild';

export interface State extends EntityState<Bild> {
  selectedId?: string | number; // which Bild record has been selected
  loaded: boolean; // has the Bild list been loaded
  error?: string | null; // last known error (if any)
}

export interface BildPartialState {
  readonly [BILD_FEATURE_KEY]: State;
}

export const bildAdapter: EntityAdapter<Bild> = createEntityAdapter<Bild>();

export const initialState: State = bildAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const bildReducer = createReducer(
  initialState,
  on(BildActions.loadBild, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(BildActions.loadBildSuccess, (state, { bild }) =>
    bildAdapter.upsertMany(bild, { ...state, loaded: true })
  ),
  on(BildActions.loadBildFailure, (state, { error }) => ({ ...state, error }))
);

export function reducer(state: State | undefined, action: Action) {
  return bildReducer(state, action);
}
