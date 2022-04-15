import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as DeskriptorActions from './deskriptor.actions';
import { Deskriptor } from '../../entities/deskriptor';

export const DESKRIPTOR_FEATURE_KEY = 'deskriptoren-deskriptor';

export interface State extends EntityState<Deskriptor> {
  selectedId?: string | number; // which Deskriptor record has been selected
  loaded: boolean; // has the Deskriptor list been loaded
  error?: string | null; // last known error (if any)
  suchliste: Deskriptor[];
}

export interface DeskriptorPartialState {
  readonly [DESKRIPTOR_FEATURE_KEY]: State;
}

export const deskriptorAdapter: EntityAdapter<Deskriptor> =
  createEntityAdapter<Deskriptor>();

export const initialState: State = deskriptorAdapter.getInitialState({
  // set initial required properties
  loaded: false,
  suchliste: []
});

const deskriptorReducer = createReducer(
  initialState,

  on(DeskriptorActions.loadDeskriptoren, (state) => ({
    ...state,
    loaded: false,
    error: null,
  })),

  on(DeskriptorActions.loadDeskriptorenSuccess, (state, { deskriptor }) =>
    deskriptorAdapter.upsertMany(deskriptor, { ...state, loaded: true })
  ),

  on(DeskriptorActions.loadDeskriptorenFailure, (state, { error }) => ({
    ...state,
    error,
  })),

  on(DeskriptorActions.deskriptorAddedToSearchList, (state, { deskriptor }) => {
    return {...state, suchliste: [...state.suchliste, deskriptor]};
  }),

  on(DeskriptorActions.deskriptorRemovedFromSearchList, (state, { deskriptor }) => {

    const neueSuchliste: Deskriptor[] = [];

    state.suchliste.forEach(
      d => {
        if (d.id !== deskriptor.id) {
          neueSuchliste.push(d);
        }
      }
    );

    return {...state, suchliste: neueSuchliste};

  }),
);

export function reducer(state: State | undefined, action: Action) {
  return deskriptorReducer(state, action);
}
