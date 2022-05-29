import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { Raetsel, RaetselDetails } from '../../entities/raetsel';

export const RAETSEL_FEATURE_KEY = 'raetsel';

export interface RaetselState extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  page: Raetsel[];
  raetselDetails?: RaetselDetails; // details eines Raetsels, das in der Detailansicht oder im Editor angezeigt wird
  saveSuccessMessage?: string
}

export interface RaetselPartialState {
  readonly [RAETSEL_FEATURE_KEY]: RaetselState;
}

export const raetselAdapter: EntityAdapter<Raetsel> =
  createEntityAdapter<Raetsel>();

export const initialState: RaetselState = raetselAdapter.getInitialState({
  loaded: false,
  page: []
});

const raetselReducer = createReducer(
  initialState,

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

  on(RaetselActions.raetsellisteCleared, (state, _action) => ({
    ...state,
    selectedId: undefined,
    loaded: false,
    page: []
  })),

  on(RaetselActions.raetselDetailsLoaded, (state, action) => {

    return { ...state, selectedId: action.raetselDetails.id, raetselDetails: action.raetselDetails };
  }),

  on(RaetselActions.raetselSaved, (state, action) => {

    const raetselDetails: RaetselDetails = action.raetselDetails;
    const raetsel: Raetsel = {
      id: raetselDetails.id,
      name: raetselDetails.name,
      schluessel: raetselDetails.schluessel,
      deskriptoren: raetselDetails.deskriptoren
    };

    return raetselAdapter.upsertOne(raetsel, {
      ...state,
      loaded: true,
      raetselDetails: raetselDetails,
      saveSuccessMessage: action.successMessage
    });
  })
);

export function reducer(state: RaetselState | undefined, action: Action) {
  return raetselReducer(state, action);
}
