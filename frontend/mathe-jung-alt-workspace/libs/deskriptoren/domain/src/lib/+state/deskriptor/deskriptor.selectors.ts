import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';
import { MjaSetUtils } from '@mathe-jung-alt-workspace/shared/utils';
import {
  DESKRIPTOR_FEATURE_KEY,
  DeskriptorenState,
  deskriptorAdapter,
} from './deskriptor.reducer';

// Lookup the 'Deskriptor' feature state managed by NgRx
const getDeskriptorState = createFeatureSelector<DeskriptorenState>(DESKRIPTOR_FEATURE_KEY);

const { selectAll, selectEntities } = deskriptorAdapter.getSelectors();

export const getDeskriptorLoaded = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.loaded
);

export const getDeskriptorError = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.error
);

export const getAllDeskriptor = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => selectAll(state)
);

export const getDeskriptorEntities = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => selectEntities(state)
);

export const getSelectedId = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.selectedId
);

export const getSelected = createSelector(
  getDeskriptorEntities,
  getSelectedId,
  (entities, selectedId) => selectedId && entities[selectedId]
);

export const getSuchliste = createSelector(
  getDeskriptorState,
  (state: DeskriptorenState) => state.suchliste
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
