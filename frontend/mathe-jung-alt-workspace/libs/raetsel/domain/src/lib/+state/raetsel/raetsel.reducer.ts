import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { Raetsel } from '../../entities/raetsel';

export const RAETSEL_FEATURE_KEY = 'raetsel-raetsel';

export interface RaetselState extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  page: Raetsel[];
}

export interface RaetselPartialState {
  readonly [RAETSEL_FEATURE_KEY]: RaetselState;
}

export const raetselAdapter: EntityAdapter<Raetsel> =
  createEntityAdapter<Raetsel>();

export const initialState: RaetselState = raetselAdapter.getInitialState({
  // set initial required properties
  loaded: false,
  page: []
});

const raetselReducer = createReducer(
  initialState,

  on(RaetselActions.findRaetsel, (state) => ({
    ...state,
    loaded: false
  })),

  on(RaetselActions.findRaetselSuccess, (state, { raetsel }) =>

    raetselAdapter.setAll(raetsel, {
      ...state,
      loaded: true,
      page: raetsel.slice(0, 5)
    })

  ),

  on(RaetselActions.pageSelected, (state, { raetsel }) => ({
    ...state, page: raetsel
  })),
);

export function reducer(state: RaetselState | undefined, action: Action) {
  return raetselReducer(state, action);
}
