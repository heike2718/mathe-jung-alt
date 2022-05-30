import { PageDefinition, Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { EditRaetselPayload, Raetsel, RaetselDetails } from '../../entities/raetsel';

export const prepareSearch = createAction(
  '[Raetsel] prepareSearch'
);

export const selectPage = createAction(
  '[Raetsel] setPageDefinition',
  props<{ pageDefinition: PageDefinition }>()
);

export const countRaetsel = createAction(
  '[Raetsel] count'
);

export const raetselCounted = createAction(
  '[Raetsel] count completed',
  props<{ anzahl: number }>()
);

export const findRaetsel = createAction(
  '[Raetsel] find Raetsel'
);

export const findRaetselSuccess = createAction(
  '[Raetsel] find Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const pageSelected = createAction(
  '[Raetsel] page selected',
  props<{ raetsel: Raetsel[] }>()
);

export const raetsellisteCleared = createAction(
  '[Raetsel] raetselliste cleared ');

export const raetselSelected = createAction(
  '[Raetsel] raetselliste raetsel selected',
  props<{ raetsel: Raetsel }>()
);

export const raetselDetailsLoaded = createAction(
  '[Raetsel] details loaded',
  props<{ raetselDetails: RaetselDetails }>()
);

export const startEditRaetsel = createAction(
  '[Raetsel]  start edit',
  props<{ raetselDetails: RaetselDetails }>()
);

export const startSaveRaetsel = createAction(
  '[Raetsel] start save',
  props<{ editRaetselPayload: EditRaetselPayload }>()
);

export const raetselSaved = createAction(
  '[Raetsel] raetsel saved',
  props<{ raetselDetails: RaetselDetails, successMessage: string }>()
);



