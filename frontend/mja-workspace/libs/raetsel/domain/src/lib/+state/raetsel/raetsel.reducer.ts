import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { initialRaetselDetailsContent, Raetsel, RaetselDetails, RaetselDetailsContent } from '../../entities/raetsel';
import { PaginationState, initialPaginationState } from '@mja-workspace/suchfilter/domain';

export const RAETSEL_FEATURE_KEY = 'raetsel-raetsel';

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

  on(RaetselActions.raetselDetailsLoaded, (state, action) => {

    return { ...state, selectedId: action.raetselDetails.id, raetselDetailsContent: { ...state.raetselDetailsContent, raetsel: action.raetselDetails } };
  }),

  on(RaetselActions.outputGenerated, (state, action) => {

    if (state.raetselDetailsContent && state.raetselDetailsContent.raetsel) {
      const neueDetails: RaetselDetails = { ...state.raetselDetailsContent.raetsel, imageFrage: action.images.imageFrage, imageLoesung: action.images.imageLoesung };
      const neuerContent = { ...state.raetselDetailsContent, raetsel: neueDetails };
      return { ...state, raetselDetailsContent: neuerContent, generatingOutput: false };
    }

    return { ...state };
  }),

  on(RaetselActions.raetsellisteCleared, (state, _action) => ({
    ...state,
    paginationState: initialPaginationState,
    selectedId: undefined,
    loaded: false,
    page: [],
    saveSuccessMessage: undefined
  })),

);

export function reducer(state: RaetselState | undefined, action: Action) {
  return raetselReducer(state, action);
}
