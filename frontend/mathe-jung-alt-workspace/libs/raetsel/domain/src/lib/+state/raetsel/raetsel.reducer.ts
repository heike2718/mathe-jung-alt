import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { initialRaetselDetailsContent, Raetsel, RaetselDetails, RaetselDetailsContent } from '../../entities/raetsel';
import { initialPaginationState, PaginationState } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { noopAction } from '@mathe-jung-alt-workspace/shared/utils';

export const RAETSEL_FEATURE_KEY = 'raetsel';

export interface RaetselState extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  page: Raetsel[];
  // raetselDetails?: RaetselDetails; // details eines Raetsels, das in der Detailansicht oder im Editor angezeigt wird
  saveSuccessMessage?: string,
  paginationState: PaginationState;
  raetselDetailsContent: RaetselDetailsContent;
  generatingOutput: boolean;
}

export interface RaetselPartialState {
  readonly [RAETSEL_FEATURE_KEY]: RaetselState;
}

export const raetselAdapter: EntityAdapter<Raetsel> =
  createEntityAdapter<Raetsel>();

export const initialState: RaetselState = raetselAdapter.getInitialState({
  loaded: false,
  anzahlTreffer: 0,
  pageSize: 10,
  pageIndex: 0,
  sortDirection: 'asc',
  page: [],
  paginationState: initialPaginationState,
  raetselDetailsContent: initialRaetselDetailsContent,
  generatingOutput: false
});

const raetselReducer = createReducer(
  initialState,

  on(RaetselActions.selectPage, (state, action) => {
    return {
      ...state,
      paginationState: {
        ...state.paginationState,
        pageIndex: action.pageDefinition.pageIndex,
        pageSize: action.pageDefinition.pageSize,
        sortDirection: action.pageDefinition.sortDirection
      }
    }
  }),

  on(RaetselActions.raetselCounted, (state, action) => {
    return {
      ...state,
      paginationState: {
        ...state.paginationState,
        anzahlTreffer: action.anzahl
      }
    };
  }),

  on(RaetselActions.findRaetselSuccess, (state, { raetsel }) =>

    raetselAdapter.setAll(raetsel, {
      ...state,
      loaded: true,
      page: raetsel
    })

  ),

  on(RaetselActions.raetsellisteCleared, (state, _action) => ({
    ...state,
    paginationState: initialPaginationState,
    selectedId: undefined,
    loaded: false,
    page: [],
    saveSuccessMessage: undefined
  })),

  on(RaetselActions.raetselDetailsLoaded, (state, action) => {

    return { ...state, selectedId: action.raetselDetails.id, raetselDetailsContent: { ...state.raetselDetailsContent, raetsel: action.raetselDetails } };
  }),

  on(RaetselActions.editRaetsel, (state, action) => {
    return { ...state, raetselDetailsContent: action.raetselDetailsContent }
  }),

  on(RaetselActions.cancelEdit, (state, _action) => {

    return { ...state, raetselDetailsContent: initialRaetselDetailsContent };
  }),

  on(RaetselActions.generateOutput, (state, _action) => {
    return { ...state, generatingOutput: true }
  }),

  on(RaetselActions.outputGenerated, (state, action) => {

    if (state.raetselDetailsContent && state.raetselDetailsContent.raetsel) {
      const neueDetails: RaetselDetails = { ...state.raetselDetailsContent.raetsel, imageFrage: action.images.imageFrage, imageLoesung: action.images.imageLoesung };
      const neuerContent = { ...state.raetselDetailsContent, raetsel: neueDetails };
      return { ...state, raetselDetailsContent: neuerContent, generatingOutput: false };
    }

    return { ...state };
  }),

  on(noopAction, (state, _action) => {
    return { ...state, generatingOutput: false };
  }),

  on(RaetselActions.raetselSaved, (state, action) => {

    const raetselDetails: RaetselDetails = action.raetselDetails;
    const raetsel: Raetsel = {
      id: raetselDetails.id,
      name: raetselDetails.name,
      status: raetselDetails.status,
      schluessel: raetselDetails.schluessel,
      deskriptoren: raetselDetails.deskriptoren
    };

    return raetselAdapter.upsertOne(raetsel, {
      ...state,
      loaded: true,
      raetselDetailsContent: {...state.raetselDetailsContent, raetsel: raetselDetails},
      saveSuccessMessage: action.successMessage
    });
  }),


);

export function reducer(state: RaetselState | undefined, action: Action) {
  return raetselReducer(state, action);
};
