import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Quelle } from '../../entities/quelle';

export const findQuellen = createAction(
  '[Quelle] find quellen',
  props<{suchfilter: Suchfilter}>()
);

export const quellenFound = createAction(
  '[Quelle] found',
  props<{ quellen: Quelle[] }>()
);

export const quelleSelected = createAction(
  '[Quelle] selected',
  props<{quelle: Quelle}>()
);

export const selectPage = createAction('[Quelle] select Page',
  props<{ sortDirection: string, pageIndex: number, pageSize: number }>());

export const pageSelected = createAction('[Quelle] page selected',
  props<{ quellen: Quelle[] }>()
);

export const findQuelle = createAction(
  '[Quelle] find by id',
  props<{uuid: string}>()
);

export const quelleFound = createAction(
  '[Quelle] found by id',
  props<{quelle: Quelle}>()
);

export const quellenlisteCleared = createAction('[Quelle] quellenliste cleared ');

