import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';
import { MjaEntity, MjaSetUtils } from '@mathe-jung-alt-workspace/shared/utils';
import {
  DESKRIPTOR_FEATURE_KEY,
  State,
  DeskriptorPartialState,
  deskriptorAdapter,
} from './deskriptor.reducer';

// Lookup the 'Deskriptor' feature state managed by NgRx
export const getDeskriptorState = createFeatureSelector<
  DeskriptorPartialState,
  State
>(DESKRIPTOR_FEATURE_KEY);

const { selectAll, selectEntities } = deskriptorAdapter.getSelectors();

export const getDeskriptorLoaded = createSelector(
  getDeskriptorState,
  (state: State) => state.loaded
);

export const getDeskriptorError = createSelector(
  getDeskriptorState,
  (state: State) => state.error
);

export const getAllDeskriptor = createSelector(
  getDeskriptorState,
  (state: State) => selectAll(state)
);

export const getDeskriptorEntities = createSelector(
  getDeskriptorState,
  (state: State) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getDeskriptorState,
  (state: State) => state.selectedId
);

export const getSelected = createSelector(
  getDeskriptorEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

export const getSuchliste = createSelector(
  getDeskriptorState,
  (state: State) => state.suchliste
);

export const getRestliste = createSelector(
  getAllDeskriptor,
  getSuchliste,
  (deskriptoren: Deskriptor[], suchliste: Deskriptor[]) => getDifferenzmenge(deskriptoren, suchliste)
);

// ////////////////////////////
// private helper functions
// ////////////////////////////

function getDifferenzmenge(alle: Deskriptor[], auszuschliessen: Deskriptor[]): Deskriptor[] {

  const setUtils: MjaSetUtils<Deskriptor> = new MjaSetUtils();
  const result: Deskriptor[] = setUtils.getDifferenzmenge(setUtils.toMjaEntityArray(alle), setUtils.toMjaEntityArray(auszuschliessen));
  return result;
}
