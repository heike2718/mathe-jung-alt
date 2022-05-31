import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as QuellenActions from './quelle.actions';
import { Quelle } from '../../entities/quelle';

export const QUELLE_FEATURE_KEY = 'quellen';

export interface QuellenState extends EntityState<Quelle> {
  selectedId?: string | number; // which Quelle record has been selected
  loaded: boolean; // has the Quelle list been loaded
  page: Quelle[];
}

export interface QuellePartialState {
  readonly [QUELLE_FEATURE_KEY]: QuellenState;
}

export const quelleAdapter: EntityAdapter<Quelle> =
  createEntityAdapter<Quelle>();

export const initialState: QuellenState = quelleAdapter.getInitialState({
  loaded: false,
  page: []
});

const quelleReducer = createReducer(
  initialState,


  on(QuellenActions.quellenFound, (state, { quellen }) =>
    quelleAdapter.setAll(quellen, {
      ...state,
      loaded: true,
      page: quellen.slice(0, 5)
    })
  ),

  on(QuellenActions.quelleFound, (state, { quelle }) => {

    const quellen: Quelle[] = [];
    quellen.push(quelle);

    return quelleAdapter.addOne(quelle, {
      ...state,
      selectedId: quelle.id,
      loaded: false,
      page: quellen.slice(0, 5)
    })
  }),

  on(QuellenActions.quelleSelected, (state, { quelle }) => {
    return { ...state, selectedId: quelle.id };
  }),

  on(QuellenActions.pageSelected, (state, { quellen }) => ({
    ...state, page: quellen, selectedId: undefined, selectedQuelle: undefined
  })),

  on(QuellenActions.quellenlisteCleared, (state, _action) => ({
    ...state,
    selectedId: undefined,
    loaded: false,
    page: []
  }))
);

export function reducer(state: QuellenState | undefined, action: Action) {
  return quelleReducer(state, action);
}
