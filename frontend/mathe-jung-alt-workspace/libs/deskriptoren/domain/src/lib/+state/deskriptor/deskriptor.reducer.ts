import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as DeskriptorActions from './deskriptor.actions';
import { Deskriptor } from '../../entities/deskriptor';

export const DESKRIPTOR_FEATURE_KEY = 'deskriptoren';

export interface DeskriptorenState extends EntityState<Deskriptor> {
  selectedId?: string | number; // which Deskriptor record has been selected  
  loaded: boolean; // has the Deskriptor list been loaded
}

export interface DeskriptorenPartialState {
  readonly [DESKRIPTOR_FEATURE_KEY]: DeskriptorenState;
}

export const deskriptorAdapter: EntityAdapter<Deskriptor> =
  createEntityAdapter<Deskriptor>();

const initialState: DeskriptorenState = deskriptorAdapter.getInitialState({
  // set initial required properties
  loaded: false,
});

const deskriptorReducer = createReducer(
  initialState,

  on(DeskriptorActions.loadDeskriptorenSuccess, (state, { deskriptor }) =>
    deskriptorAdapter.setAll(deskriptor, { ...state, loaded: true, auswahlliste:[], suchliste: [] })
  ),
);

export function reducer(state: DeskriptorenState | undefined, action: Action) {
  return deskriptorReducer(state, action);
}
