import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';

import * as RaetselActions from './raetsel.actions';
import { initialRaetselDetailsContent, Raetsel, RaetselDetails, RaetselDetailsContent } from '../../entities/raetsel';
import { PaginationState, initialPaginationState } from '@mja-workspace/suchfilter/domain';
import { SelectableItem } from '@mja-workspace/shared/util-mja';
import { state } from '@angular/animations';

export const RAETSEL_FEATURE_KEY = 'raetsel-raetsel';

export interface RaetselState extends EntityState<Raetsel> {
  selectedId?: string | number; // which Raetsel record has been selected
  loaded: boolean; // has the Raetsel list been loaded
  page: Raetsel[];
  // raetselDetails?: RaetselDetails; // details eines Raetsels, das in der Detailansicht oder im Editor angezeigt wird
  saveSuccessMessage?: string,
  paginationState: PaginationState;
  raetselDetailsContent: RaetselDetailsContent;
  selectableDeskriptoren: SelectableItem[];
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
  generatingOutput: false,
  selectableDeskriptoren: []
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

  on(RaetselActions.findRaetselSuccess, (state, { suchergebnis }) =>

    raetselAdapter.setAll(suchergebnis.treffer, {
      ...state,
      loaded: true,
      page: suchergebnis.treffer,
          paginationState: {
        ...state.paginationState,
        anzahlTreffer: suchergebnis.trefferGesamt
      }
    })

  ),

  on(RaetselActions.raetselDetailsLoaded, (state, action) => {

    return { ...state, selectedId: action.raetselDetails.id, raetselDetailsContent: { ...state.raetselDetailsContent, raetsel: action.raetselDetails } };
  }),

  on(RaetselActions.raetselDeskriptorenLoaded, (state, action) => ({
    ...state,
    selectableDeskriptoren: action.selectableDeskriptoren
  })),

  on(RaetselActions.editRaetsel, (state, action) => ({ ...state, raetselDetailsContent: action.raetselDetailsContent })),

  on(RaetselActions.outputGenerated, (state, action) => {

    if (state.raetselDetailsContent && state.raetselDetailsContent.raetsel) {
      const neueDetails: RaetselDetails = { ...state.raetselDetailsContent.raetsel, imageFrage: action.images.imageFrage, imageLoesung: action.images.imageLoesung };
      const neuerContent = { ...state.raetselDetailsContent, raetsel: neueDetails };
      return { ...state, raetselDetailsContent: neuerContent, generatingOutput: false };
    }

    return { ...state };
  }),

  on(RaetselActions.raetselSaved, (state, action) => {

    const details: RaetselDetails = action.raetselDetails;

    const raetsel: Raetsel = {
      id: details.id,
      deskriptoren: details.deskriptoren,
      name: details.name,
      schluessel: details.name,
      status: details.status,
      kommentar: details.kommentar
    }

    let firstState: RaetselState;

    if (action.insert) {
      firstState = raetselAdapter.upsertOne(raetsel, state);
    } else {
      const update: Update<Raetsel> = {
        id: details.id,
        changes: raetsel
      };
      firstState = raetselAdapter.updateOne(update, state);
    }

    const selectableDeskriptoren: SelectableItem[] = [];
    details.deskriptoren.forEach(d => {
      selectableDeskriptoren.push({
        id: d.id,
        name: d.name,
        selected: true
      })
    });

    return { ...firstState, raetselDetailsContent: 
      { ...firstState.raetselDetailsContent, kontext: 'RAETSEL', quelleId: details.quelleId, selectableDeskriptoren: selectableDeskriptoren, raetsel: details } };
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
