import { Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';

export const loadDeskriptoren = createAction(
  '[Deskriptor] Load Deskriptor',
  props<{ kontext: Suchkontext }>());

export const loadDeskriptorenSuccess = createAction(
  '[Deskriptor] Load Deskriptor Success',
  props<{ deskriptor: Deskriptor[] }>()
);

export const setSuchkontext = createAction(
  '[Deskriptor] set suchkontext',
  props<{ kontext: Suchkontext }>()
)

export const deskriptorAddedToSearchList = createAction(
  '[Deskriptor] Deskriptor Added To Search List',
  props<{deskriptor: Deskriptor}>()
);

export const deskriptorRemovedFromSearchList = createAction(
  '[Deskriptor] Deskriptor Removed From Search List',
  props<{deskriptor: Deskriptor}>()
);
