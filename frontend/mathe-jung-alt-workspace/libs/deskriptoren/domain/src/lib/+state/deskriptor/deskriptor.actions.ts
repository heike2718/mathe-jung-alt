import { createAction, props } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';

export const loadDeskriptor = createAction('[Deskriptor] Load Deskriptor');

export const loadDeskriptorSuccess = createAction(
  '[Deskriptor] Load Deskriptor Success',
  props<{ deskriptor: Deskriptor[] }>()
);

export const loadDeskriptorFailure = createAction(
  '[Deskriptor] Load Deskriptor Failure',
  props<{ error: any }>()
);

export const deskriptorAddedToSearchList = createAction(
  '[Deskriptor] Deskriptor Added To Search List',
  props<{deskriptor: Deskriptor}>()
);

export const deskriptorRemovedFromSearchList = createAction(
  '[Deskriptor] Deskriptor Removed From Search List',
  props<{deskriptor: Deskriptor}>()
);
