import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as BildActions from './bild.actions';
import { Bild } from '../../entities/bild';

export const BILD_FEATURE_KEY = 'bilder';

export interface BilderState extends EntityState<Bild> {
  selectedId?: string | number; // which Bild record has been selected
  loaded: boolean; // has the Bild list been loaded
  error?: string | null; // last known error (if any)
}

export interface BildPartialState {
  readonly [BILD_FEATURE_KEY]: BilderState;
}

export const bildAdapter: EntityAdapter<Bild> = createEntityAdapter<Bild>();

export const initialState: BilderState = bildAdapter.getInitialState({
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

export function reducer(state: BilderState | undefined, action: Action) {
  return bildReducer(state, action);
}
