import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as MedienActions from './medien.actions';
import { Medium } from '../../entities/medien';

export const MEDIEN_FEATURE_KEY = 'medien';

export interface MedienState extends EntityState<Medium> {
  selectedId?: string | number; // which Medien record has been selected
  loaded: boolean; // has the Medien list been loaded
  error?: string | null; // last known error (if any)
}

export interface MedienPartialState {
  readonly [MEDIEN_FEATURE_KEY]: MedienState;
}

export const medienAdapter: EntityAdapter<Medium> =
  createEntityAdapter<Medium>();

export const initialState: MedienState = medienAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const medienReducer = createReducer(
  initialState,
  on(MedienActions.loadMedien, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),
  on(MedienActions.loadMedienSuccess, (state, { medien }) =>
    medienAdapter.upsertMany(medien, { ...state, loaded: true })
  ),
  on(MedienActions.loadMedienFailure, (state, { error }) => ({
    ...state,
    error,
  }))
);

export function reducer(state: MedienState | undefined, action: Action) {
  return medienReducer(state, action);
}
