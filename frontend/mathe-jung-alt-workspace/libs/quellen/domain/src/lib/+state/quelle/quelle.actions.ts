import { Suchfilter as QuellenActions } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Quelle } from '../../entities/quelle';

export const loadQuelle = createAction('[Quelle] Load Quelle');

export const findQuelle = createAction(
  '[Quelle] find quelle',
  props<{suchfilter: QuellenActions}>()
);

export const quellenFound = createAction(
  '[Quelle] found',
  props<{ quellen: Quelle[] }>()
);

export const findQuellenFailure = createAction(
  '[Quelle] Load Quelle Failure',
  props<{ error: any }>()
);
