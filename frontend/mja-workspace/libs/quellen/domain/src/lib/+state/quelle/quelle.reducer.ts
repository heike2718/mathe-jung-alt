import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as QuelleActions from './quelle.actions';
import { Quelle } from '../../entities/quelle';

export const QUELLE_FEATURE_KEY = 'quellen-quelle';

export interface QuellenState extends EntityState<Quelle> {
  selectedId?: string | number; // which Quelle record has been selected
  loaded: boolean; // has the Quelle list been loaded
  adminQuelle?: Quelle,
  page: Quelle[];
}

export interface QuellePartialState {
  readonly [QUELLE_FEATURE_KEY]: QuellenState;
}

export const quelleAdapter: EntityAdapter<Quelle> =
  createEntityAdapter<Quelle>();

export const initialState: QuellenState = quelleAdapter.getInitialState({
  // set initial required properties
  loaded: false,
  page: []
});

const quelleReducer = createReducer(
  initialState,


  on(QuelleActions.quellenFound, (state, { quellen }) =>
    quelleAdapter.setAll(quellen, {
      ...state,
      loaded: true,
      page: quellen.slice(0, 5)
    })
  ),

  on(QuelleActions.quelleFound, (state, { quelle }) => {

    const quellen: Quelle[] = [];
    quellen.push(quelle);

    return quelleAdapter.addOne(quelle, {
      ...state,
      selectedId: quelle.id,
      loaded: false,
      page: quellen.slice(0, 5)
    })
  }),

  on(QuelleActions.quelleAdminLoaded, (state, { quelle }) => {

    const quellen: Quelle[] = [];
    quellen.push(quelle);

    return quelleAdapter.addOne(quelle, {
      ...state,
      adminQuelle: quelle,
      loaded: false,
      page: quellen.slice(0, 5)
    })
  }),

  on(QuelleActions.quelleSelected, (state, { quelle }) => {
    return { ...state, selectedId: quelle.id };
  }),

  on(QuelleActions.pageSelected, (state, { quellen }) => ({
    ...state, page: quellen, selectedId: undefined, selectedQuelle: undefined
  })),

  on(QuelleActions.quellenlisteCleared, (state, _action) => ({
    ...state,
    selectedId: undefined,
    loaded: false,
    page: []
  }))
);

export function reducer(state: QuellenState | undefined, action: Action) {
  return quelleReducer(state, action);
}
